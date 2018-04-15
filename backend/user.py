class Player:
    def __init__(self, uid):
        self.uid = uid
        self.health = 100
        self.sheild = 0
        self.inventory = []
        self.lat = None
        self.long = None
    
    def add_item(self, item):
        self.inventory.append(item)

    def place_player(self, lat, long):
        self.lat = lat
        self.long = long
    

