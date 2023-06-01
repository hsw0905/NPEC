#! /bin/bash

ssh -i ~/Downloads/npec.pem ubuntu@3.34.42.117 <<EOF

cd ~/logs

./print_logs.sh

EOF
echo "로그 종료"
