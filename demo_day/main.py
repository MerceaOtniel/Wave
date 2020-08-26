import urllib.request
import json
import subprocess
import time
from subprocess import call
call(["amixer", "-D", "pulse", "sset", "Master", "100%"])



while True:
	req = urllib.request.urlopen("http://192.168.6.42:5000")
	req=json.loads(req.read())
	process = None

	if len(req["queue"]) > 0:
		instruction = req["queue"][0]
		print(instruction)

		if instruction == "Thumb Up":
			subprocess.run([
					"chromium", 
					"https://www.youtube.com/watch?v=rJDxpchqyqE&feature=youtu.be&t=10&fbclid=IwAR2EHVDkMEFHH4KkhH4gD94j2kGRQTj6EybIVnVuvSsiA4qNHRyGez-AaP8",
					"--disable-gpu",
					"--disable-software-rasterizer"
			])
		elif instruction == "Swiping Down":
			call(["amixer", "-D", "pulse", "sset", "Master", "0%"])
		elif instruction == "Swiping Up":
			call(["amixer", "-D", "pulse", "sset", "Master", "100%"])
