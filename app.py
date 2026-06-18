from flask import Flask, request, redirect, jsonify
import jiosaavn
import os
from traceback import print_exc
from flask_cors import CORS

app = Flask(__name__)
app.secret_key = os.environ.get("SECRET", 'thankyoutonystark#weloveyou3000')
CORS(app)

# Helper function to ensure we always return a list for the Android App
def ensure_list(data):
    if isinstance(data, dict):
        if 'results' in data:
            return data['results']
        elif 'songs' in data:
            return data['songs']
        return [data] # Return as list if it's a single object
    return data if isinstance(data, list) else []

@app.route('/')
def home():
    return redirect("https://cyberboysumanjay.github.io/JioSaavnAPI/")

@app.route('/song/')
def search():
    lyrics = False
    songdata = True
    query = request.args.get('query')
    lyrics_ = request.args.get('lyrics')
    songdata_ = request.args.get('songdata')
    
    if lyrics_ and lyrics_.lower() != 'false':
        lyrics = True
    if songdata_ and songdata_.lower() != 'true':
        songdata = False
        
    if query:
        data = jiosaavn.search_for_song(query, lyrics, songdata)
        # Extract the list so Android gets [{},{},{}] instead of {"results": [...]}
        return jsonify(ensure_list(data))
    else:
        return jsonify({"status": False, "error": 'Query is required!'})

@app.route('/song/get/')
def get_song():
    lyrics = False
    song_id = request.args.get('id')
    lyrics_ = request.args.get('lyrics')
    if lyrics_ and lyrics_.lower() != 'false':
        lyrics = True
    if song_id:
        resp = jiosaavn.get_song(song_id, lyrics)
        if not resp:
            return jsonify({"status": False, "error": 'Invalid Song ID!'})
        return jsonify(resp)
    return jsonify({"status": False, "error": 'Song ID is required!'})

@app.route('/playlist/')
def playlist():
    lyrics = False
    query = request.args.get('query')
    lyrics_ = request.args.get('lyrics')
    if lyrics_ and lyrics_.lower() != 'false':
        lyrics = True
    if query:
        id = jiosaavn.get_playlist_id(query)
        songs = jiosaavn.get_playlist(id, lyrics)
        return jsonify(ensure_list(songs))
    return jsonify({"status": False, "error": 'Query is required!'})

@app.route('/album/')
def album():
    lyrics = False
    query = request.args.get('query')
    lyrics_ = request.args.get('lyrics')
    if lyrics_ and lyrics_.lower() != 'false':
        lyrics = True
    if query:
        id = jiosaavn.get_album_id(query)
        songs = jiosaavn.get_album(id, lyrics)
        return jsonify(ensure_list(songs))
    return jsonify({"status": False, "error": 'Query is required!'})

@app.route('/lyrics/')
def lyrics():
    query = request.args.get('query')
    if query:
        try:
            if 'http' in query and 'saavn' in query:
                id = jiosaavn.get_song_id(query)
                lyrics_data = jiosaavn.get_lyrics(id)
            else:
                lyrics_data = jiosaavn.get_lyrics(query)
            return jsonify({"status": True, "lyrics": lyrics_data})
        except Exception as e:
            return jsonify({"status": False, "error": str(e)})
    return jsonify({"status": False, "error": 'Query is required!'})

@app.route('/result/')
def result():
    lyrics = False
    query = request.args.get('query')
    # CAPTURE THE 'n' PARAMETER FROM ANDROID (Defaults to 20 if not sent)
    n = int(request.args.get('n', 20)) 
    lyrics_ = request.args.get('lyrics')
    if lyrics_ and lyrics_.lower() != 'false':
        lyrics = True

    try:
        data = None
        if not query:
            return jsonify({"error": "Query required"})

        if 'saavn' not in query:
            data = jiosaavn.search_for_song(query, lyrics, True)
        elif '/song/' in query:
            song_id = jiosaavn.get_song_id(query)
            data = jiosaavn.get_song(song_id, lyrics)
        elif '/album/' in query:
            id = jiosaavn.get_album_id(query)
            data = jiosaavn.get_album(id, lyrics)
        elif '/playlist/' in query or '/featured/' in query:
            id = jiosaavn.get_playlist_id(query)
            data = jiosaavn.get_playlist(id, lyrics)
        
        # EXTRACT LIST AND SLICE TO 'n' (the number sent by Android)
        return jsonify(ensure_list(data)[:n])

    except Exception as e:
        print_exc()
        return jsonify({"status": False, "error": str(e)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5100, use_reloader=True, threaded=True)
