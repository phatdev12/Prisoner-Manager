from gtts import gTTS
import io
import pygame
import sys

text = sys.argv[1] if len(sys.argv) > 1 else "Xin chào!"

tts = gTTS(text, lang='vi')

fp = io.BytesIO()
tts.write_to_fp(fp)
fp.seek(0)

pygame.mixer.init()
pygame.mixer.music.set_volume(1.0) 
pygame.mixer.music.load(fp)
pygame.mixer.music.play()

while pygame.mixer.music.get_busy():
    pass
