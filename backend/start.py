from flask import Flask, request, abort, jsonify
import pyrebase

from game import Game
import uuid

# initialize firebase
config = {
  "apiKey": "AIzaSyAHvIfqJ59gTKpIwLbZ3Hmes3K-ESo2ECE",
  "authDomain": "hackdartmo.firebaseapp.com",
  "databaseURL": "https://hackdartmo.firebaseio.com",
  "storageBucket": "hackdartmo.appspot.com"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

#initiaize flask
app = Flask(__name__)

# a route where we will display a welcome message via an HTML template
@app.route("/")
def hello():
	return render_template('index.html', file=data)

@app.route("/bye")
def bye():
	return "Bye"

@app.route("/api/newgame/", methods=['POST'])
def create_game():
	content = request.get_json(silent=True)
	if content is None:
		abort(403)
	uid = str(uuid.uuid4())
	game = Game(uid, content['lat'], content['long'], content['radius'])
	data = {}
	db.child("games").child(uid).set(data)

# @app.route("/api/games/<gameid>/add")
# def drop_player(gameid):
# 	game = games[gameid]

@app.route("/api/getgames", methods=['GET'])
def get_games():
	games = db.child("games").get()
	data = {}
	for game in games.each():
		data[game.key()] = {'lat':game.val()['lat'], 'long':game.val()['long'], 'r':game.val()['r'], 'players':game.val()['players']}
	return jsonify(data)
