#!/usr/bin/python

import socketserver

BIND_IP = 'localhost'
BIND_PORT = 9001
BUFFER_SIZE = 4096

class Server(socketserver.BaseRequestHandler):
	def handle(self):
		# Handle connection request.
		self.data = self.request.recv(BUFFER_SIZE).strip()
		print("{} wrote:".format(self.client_address[0]))
		print(self.data)
		# just send back the same data, but upper-cased
		response = bytes("Your input has been assimilated. Thank you for your donation.", 'ascii')
		self.request.sendall(response)



if __name__ == "__main__":
	# Create the server, binding to localhost on port 9999
	with socketserver.TCPServer((BIND_IP, BIND_PORT), Server) as server:
		server.serve_forever() # insert witty joke here
