import speech_recognition as sr

def listen():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        audio = r.listen(source)
    try:
        text = r.recognize_google(audio)  # Uses Google's API 
        print(text)  # This will be read by MapViewer
    except:
        print("ERROR")

listen()