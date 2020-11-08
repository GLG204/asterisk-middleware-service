# Before to Run this application we need to install asterisk
Our Asterisk PBX will reside on Debain, So first we must set our Debian container to install asterisk.

1. Pull Debian Image to Docker

`docker pull debian`

2. Now Lets run our New Debian container using pulled image

`docker images`

Now we shall Start our container using Our Debian image

`docker run -it --name=<Container Name> --network=host <Image ID>`

3. Install Asterisk

`apt-get update`

`apt-get -y install asterisk`

4. Asterisk Configuration

`vim /etc/asterisk/rtp.conf`

Change rtpstart and rtpend parameters.

```
rtpstart=10010
rtpend=10020
```

Save and Exit
 
This will help us wo bind the ports when we running our Asterisk container. Now We shall start the asterisk.

`/etc/init.d/asterisk start`

5. Committing the Container configuration

` docker commit -m "<Commit Message>" -a "<author's name>" <ContainerID or Name to commit> <Repository>:<tag>`

6. Run our Committed Image

` docker run -itd --name=PBX-Asterisk --network=host -p 5060:5060/tcp -p 5060:5060/udp -p 10010:10010/udp -p 10011:10011/udp -p 10012:10012/udp -p 10013:10013/udp -p 10014:10014/udp -p 10015:10015/udp -p 10016:10016/udp -p 10017:10017/udp -p 10018:10018/udp -p 10019:10019/udp -p 10020:10020/udp <Commited Docker ImageID to run>`

7.  Configure the sip peers 

`docker exec -it <Your Container ID> bash`

` vim /etc/asterisk/sip.conf`

```
    [100]
  secrete=abc123
  context=home
  type=friend
  allow=ulaw,alaw
  host=dynamic

  [101]
 secrete=abc123
 context=home
 type=friend
 allow=ulaw,alaw
 host=dynamic
```

8. Dialplan configuration 

```
exten => 1234,1,Verbose("Valid Number")
exten => 4567,1,Verbose("Another Valid Number")
exten => _.!,1,Verbose("Catch all for invalid numbers")
exten => _.!,n,Verbose("Surprise - executed for all numbers!")
```
9. Complete Asterisk configuration

`service asterisk start`

`rasterisk`

# Run using docker
1. docker build . --tag asterisk-middleware-service
2. docker run -it -p9191:9191 asterisk-middleware-service:latest