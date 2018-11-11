#!/bin/bash

echo_error() {
    if [[ $1 = '' ]] ; then
        echo $1
        echo 'pw: missing operand'
    else
        echo "pw: missing operand after '$1'"
    fi
}

run_new() {
    if ! [[ $1 = '' ]] ; then
        cd out/production/PasswordManager/
        java test $1
    else
        echo_error 'new'
    fi
}

option=$1

case ${option} in
    new)
        run_new $2;;
    create)
        echo 'cp';;
    *)
        echo_error ${option}
esac

