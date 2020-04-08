FROM ubuntu:latest

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git


RUN git config --global user.name "KayteeKlink" && \
git config --global user.email kayteeklink@gmail.com && \
git config --global user.password "1010LitClub!"open 