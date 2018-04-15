from user import Player

class Game:
    def __init__(self, uid, lat, long, radius):
        self.players = []
        self.uuid = uid
        self.lat = lat
        self.long = long
        self.r = radius
    
    def addplayer(self, uid, lat, long):
        player = Player(uid)
        player.place_player(lat, long)
        self.players.append(player)

