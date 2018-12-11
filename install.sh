#!/bin/bash


# root で実行されてるか確認
if ! whoami='root'; then
    echo 'fatal: please run as a root.'
    exit
fi

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

echo -n "running '${OS}'. press enter if this is current, or CTRL-C to cancel."; read;

# mysql がインストールされてるか確認
# 実行している os が ubuntu だった場合、mysql をインストールするか尋ねる
if ! type mysql > /dev/null 2>&1; then
    echo 'warning: mysql is not installed.'
    echo 'do you want to install it? (y/N): '
    
    while :
    do
        read input

        if [[ ${input} =~ [nN] || ${input} = '' ]]; then
            echo 'mysql is required for installation.'
            exit
        fi

        if [[ ${input} =~ [yY] ]]; then
            break
        fi

        echo "error: please enter 'y' or 'n': "
    done

    if [[ ${OS} = 'Ubuntu' ]]; then
        apt install mysql
    elif [[ ${OS} = 'CentOS' ]]; then
        yum install mysql-community-server
    else
        echo 'sorry, only Ubuntu and CentOS are supported.'
        exit
    fi
fi