#!/usr/bin/python

import smtplib
#import ssl
from email.message import EmailMessage
from email.headerregistry import Address
from email.utils import make_msgid

import socketserver


#trying to use SMPT_SSL to send an email
SSL_PORT = 465
SMTP_PORT = 587
PASSWORD = "spyapp154" #CHANGE THIS TO YOUR EMAIL.
smpt_server = "smtp.gmail.com"

sender_email = "csc154emailserver@gmail.com"
target_email = "csc154target@gmail.com"

iphoneMessageBody = """\
<html>
  <head></head>
  <body>
    <p>Hi!<br>
       How are you?<br>
       Here is the <a href="https://www.apple.com/iphone/buy/">iphone</a> you wanted. <br><br><h1>Way more secure than your current android!</h1>
    </p>
  </body>
</html>
"""


BIND_IP = ''
BIND_PORT = 9001
BUFFER_SIZE = 4096

KEY_WORDS = ['iphone', 'ferrari', 'shoes']
 
class Server(socketserver.BaseRequestHandler):

    def handle(self):

        # Handle connection request.

        self.data = self.request.recv(BUFFER_SIZE).strip()

        print("{} wrote:".format(self.client_address[0]))

        print(self.data)

        message = self.data

        components = message.split(bytes("|", 'utf-8'))
        
        if (len(components) != 2):
            print("Invalid message format, scrub.")
        else:
            print("email: " + components[0].decode("utf-8"))
            print("message: " + components[1].decode("utf-8"))
            if (scanForKeywords(components[1].decode("utf-8"), KEY_WORDS)):
                sendEmail(components[0].decode("utf-8"))
            #sendEmail(components[0].decode("utf-8"), scanForKeywords(components[1].decode("utf-8"), KEY_WORDS))

        # input recieved response
        response = bytes("Your input has been assimilated. Thank you for your donation.", 'ascii')

        self.request.sendall(response)


def scanForKeywords(inputString, keywordList):
    inputWordsList = inputString.split(" ")
    keyList = keywordList
    #matchList = []

    #DEMO: Only do iphone.
    # for keyWords in keyList:
    #     for inputWords in inputWordsList:
    #         if (keyWords.lower() == inputWords.lower()):
    #             print("We have a match! " + keyWords + " = " + inputWords)
    #             matchList.append(inputWords)
    
    for inputWords in inputWordsList:
        if ("iphone" == inputWords.lower()):
            print("We have a match! " + "iphone" + " = " + inputWords)
            return True

    #return matchList
    return ""
    



#def sendEmail(inputEmailString, keywordMatchesList):
def sendEmail(targetEmail):
    #context = ssl.create_default_context()
    #matches_str = ' '.join(keywordMatchesList)

    #print("entered into the send email function")
    
    PASSWORD = "enter your password here" #input("\nenter the password for the sender's email: ")

    msg = EmailMessage()
    msg['Subject'] = "iPhone Sales"
    msg['From'] = Address(sender_email)
    msg['To'] = targetEmail
    msg.set_content("Hi, here is the iphone you wanted: https://www.apple.com/iphone/buy/")
    asparagus_cid = make_msgid()
    msg.add_alternative("""\
<html>
  <head></head>
  <body>
    <img src="cid:{asparagus_cid}" />
    <p>Hi!<br>
       How are you?<br>
       Here is the <a href="https://www.apple.com/iphone/buy/">iphone</a> you wanted. <br><br><h1>Way more secure than your current android!</h1>
    </p>
  </body>
</html>
""".format(asparagus_cid=asparagus_cid[1:-1]), subtype='html')
    with open("apple.png", 'rb') as img:
        msg.get_payload()[1].add_related(img.read(), 'image', 'png', cid=asparagus_cid)

    mailServer = smtplib.SMTP('smtp.gmail.com',SMTP_PORT)
    mailServer.ehlo()
    mailServer.starttls()
    mailServer.login(sender_email, PASSWORD)
    #mailServer.sendmail(sender_email, inputEmailString, matches_str)
    #mailServer.sendmail(sender_email, targetEmail, emailBody)
    mailServer.send_message(msg)
    mailServer.close()








if __name__ == "__main__":

    # Create the server, binding to localhost on port 9999

    with socketserver.TCPServer((BIND_IP, BIND_PORT), Server) as server:

        server.serve_forever() # insert witty joke here