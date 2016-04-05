class Converter:
    # Convert a unicode string into an array of words
	# retain only letters and no single letter words
    def stringToArray(value):
        for c in value:
			# [65-90] and [97-122]
            if (ord(c) < 65 or ord(c) > 122 or (ord(c) > 90 and ord(c) < 97)):
                value = value.replace(c, " ")
        value = value.split(' ')
        for word in value:
            if (len(word) < 2):
                value.remove(word)
        while('' in value):
            value.remove('')
        
        return value