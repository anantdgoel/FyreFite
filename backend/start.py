from flask import Flask, request, abort, jsonify, render_template
from flask_cors import CORS
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
CORS(app)

# a route where we will display a welcome message via an HTML template
@app.route("/")
def hello():
	return render_template('index.html', message="hello")

@app.route("/bye")
def bye():
	return "Bye"

@app.route("/api/newgame/", methods=['POST'])
def create_game():
	uid = str(uuid.uuid4())
	data = {'uid':uid, 'lat':request.args.get('lat'), 'long':request.args.get('long'), 'r':request.args.get('radius'), 'name':request.args.get('name', 'admin':request.args.get('admin_uid'))}
	db.child("games").child(uid).set(data)
	resp_obj = {'uid':uid}
	return jsonify(resp_obj)

# @app.route("/api/games/<gameid>/add")
# def drop_player(gameid):
# 	game = games[gameid] 

@app.route("/api/getgames/", methods=['GET'])
def get_games():
	games = db.child("games").get()
	return jsonify(games.val())

@app.route("/api/games/<gameid>", methods=['GET'])
def get_game(gameid):
	game_by_id = db.child("games/"+gameid).get()
	return jsonify(game_by_id.val())

@app.route("/api/games/<gameid>/players", methods=['GET'])
def players(gameid):
	players = db.child("games/"+gameid).child("players").get()
	return jsonify(players.val()) 

@app.route("/api/<gameid>/join/<pid>", methods=['POST'])
def add_to_game(gameid, pid):
	players = db.child("games/"+gameid+"/players").child(pid).update({"lat":request.args.get('lat'), "long":request.args.get('long')})
	return jsonify(players)
