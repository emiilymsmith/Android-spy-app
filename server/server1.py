#!/usr/bin/python


import smtplib
#import ssl
 
import socketserver

#trying to use SMPT_SSL to send an email
SSL_PORT = 465
SMTP_PORT = 587
PASSWORD = ""
smpt_server = "smtp.gmail.com"

sender_email = "csc154emailserver@gmail.com"
target_email = "csc154target@gmail.com"


BIND_IP = 'localhost'
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
            sendEmail(components[0].decode("utf-8"), scanForKeywords(components[1].decode("utf-8"), KEY_WORDS))

        # input recieved response
        response = bytes("Your input has been assimilated. Thank you for your donation.", 'ascii')

        self.request.sendall(response)


def scanForKeywords(inputString, keywordList):
    inputWordsList = inputString.split(" ")
    keyList = keywordList
    matchList = []

    for keyWords in keyList:
        for inputWords in inputWordsList:
            if (keyWords.lower() == inputWords.lower()):
                print("We have a match! " + keyWords + " = " + inputWords)
                matchList.append(inputWords)
    
    return matchList
    

def sendEmail(inputEmailString, keywordMatchesList):
    #context = ssl.create_default_context()
    matches_str = ' '.join(keywordMatchesList)

    #print("entered into the send email function")
    
    PASSWORD = input("\nenter the password for the sender's email: ")

    mailServer = smtplib.SMTP('smtp.gmail.com',SMTP_PORT)
    mailServer.ehlo()
    mailServer.starttls()
    mailServer.login(sender_email, PASSWORD)
    mailServer.sendmail(sender_email, target_email, matches_str)
    mailServer.close()








if __name__ == "__main__":

    # Create the server, binding to localhost on port 9999

    with socketserver.TCPServer((BIND_IP, BIND_PORT), Server) as server:

        server.serve_forever() # insert witty joke here