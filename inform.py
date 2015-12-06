import sys
# import some email, sms client
# looking at yagmail as an easy client that uses gmail for prototyping
# possibly twilio for sms, phone stuff. It will cost if scalling up but has free tier

"""
run program when sending an email or such to a user
pass in what type of communication and where in the db to look into for the msg and
user information
"""

def email(to, msg):
    # send email with msg
    # probably have html templated msg in the fs

if (sys.argv[1] == "email"):
    # sending an email to a user, check for user name and msg ident
    user = sys.argv[2]
    ident = sys.argv[3]

elif (sys.argv[1] == "text"):
    # send an sms msg to a user

