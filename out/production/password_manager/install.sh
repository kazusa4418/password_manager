#!/bin/bash
cd `dirname $0`

RELEASE_FILE=/etc/os-release
if grep '^NAME="CentOS' "${RELEASE_FILE}" > /dev/null; then
    OS=CentOS
elif grep '^NAME="Manjaro' "${RELEASE_FILE}" > /dev/null; then
    OS="Manjaro Linux"
elif grep '^NAME="Amazon' "${RELEASE_FILE}" > /dev/null; then
    OS="Amazon Linux"
elif grep '^NAME="Ubuntu' "${RELEASE_FILE}" > /dev/null; then
    OS=Ubuntu
else
    echo "fatal: Your platform is not supported."
    exit
fi

read -rp "running '${OS}'. press enter if this is current, or CTRL-C to cancel." tmp;echo

# mysql がインストールされてるか確認
# インストールされていなかった場合、mysql をインストールするか尋ねる
if type mysql > /dev/null 2>&1; then
    echo 'found mysql-server!'
else
    echo 'warning: mysql is not installed.'
    echo -n 'do you want to install it? (y/N): '

    while :
    do
        read input

        if [[ ${input} =~ [nN] ]] || [[ ${input} = '' ]]; then
            echo 'error: mysql is required for installation.'
            exit
        fi

        if [[ ${input} =~ [yY] ]]; then
            break
        fi

        echo -n "error: please enter 'y' or 'n': "
    done

    if [[ ${OS} = 'Ubuntu' ]]; then
        sudo apt -y install mysql-server
    elif [[ ${OS} = 'CentOS' ]]; then
        sudo yum -y install mysql-community-server
    else
        echo 'sorry, only Ubuntu and CentOS are supported.'
        exit
    fi
    echo
    echo 'mysql installation was successfully completed!'
fi

# mysql に pw 用のデータベースとユーザーを作成するか尋ねる
read -rp 'would you like to create a database, tables and users for pw in mysql? (y/N): ' input;echo
while :
do
    if [[ ${input} =~ [nN] ]] || [[ ${input} = '' ]]; then
        echo -n 'warning: did not set up mysql. please set up mysql for yourself'
        for i in 0 1 2;
        do
            echo -n '.'
            sleep 1
        done
        echo; echo;

        break
    fi

    if [[ ${input} =~ [yY] ]]; then
        echo 'it need to connect to mysql with an existing mysql user with a password to set up mysql.'
        read -rp 'please type username: ' username; if [[ ${username} = '' ]]; then username='""'; fi
        read -rp 'please type password: ' password; if [[ ${password} = '' ]]; then password='""'; fi

        if ! [[ -f './mysql.properties' ]]; then
            cp mysql.properties.example mysql.properties
        fi
        java -classpath lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar:./ Migrate localhost ${username} ${password}

        if [[ $? = 0 ]]; then
            echo 'mysql set up was successfully completed!'
            break
        elif [[ $? = 1 ]]; then
            echo 'error: Access denied for user root@localhost.'
            echo 'fatal: Installation failed.'
            exit
        fi
    fi

    read -rp "error: please enter 'y' or 'n': " input;echo
done

# path を通す
echo 'writing path setting to ~/.bashrc...'

PW_ROOT=$(pwd)
EXPORT_PATH="export PATH=${PW_ROOT}:"
EXPORT_PATH+='$PATH'
echo '# pw settings' >> ~/.bashrc
echo ${EXPORT_PATH} >> ~/.bashrc
echo "written '${EXPORT_PATH}' to ~/.bashrc."; echo;

echo 'All done! Installation finished.'
echo "please run 'source ~/.bashrc' and reload '.bashrc'!"
