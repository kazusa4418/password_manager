#!/bin/bash

echo_error() {
    if [[ $1 = '' ]]; then
        echo 'pw: missing operand'
    else
        echo "pw: missing operand after '$1'"
    fi
}

run_command() {
    if ! [[ $2 = '' ]]; then
        cd out/production/password_manager/
        java -classpath lib/mysql-connector-java-8.0.12/mysql-connector-java-8.0.12.jar:./ $1 $2
    else
        echo_error ${1,,}
    fi
}

option=$1
third_argument=$3

if ! [[ ${third_argument} = '' ]]; then
    echo 'pw: too many arguments'
    exit
fi

case ${option} in
    new)
        run_command New $2;;
    update)
        run_command Update $2;;
    cp)
        run_command Cp $2;;
    *)
        echo_error ${option}
esac

