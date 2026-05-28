import discord
import requests
import json
import os
import re
from datetime import datetime
from dotenv import load_dotenv

load_dotenv()

TOKEN = os.getenv("DISCORD_TOKEN")
TARGET_CHANNEL_ID = 1443367005192454157  # canal général

API_URL = "http://localhost:7070/avis"
SIGLE_REGEX = r"\b[A-Za-z]{3}\d{4}\b"

# -------------------------
# Keywords for difficulty
# -------------------------
DIFFICULTY_KEYWORDS = {
    1: ["très facile", "donné", "cadeau", "trivial", "trop simple"],
    2: ["facile", "simple", "chill", "assez facile", "pas trop dur", "manageable", "easy"],
    3: ["moyen", "correct", "normal", "raisonnable", "correcte"],
    4: ["dur", "difficile", "pas facile" , "compliqué", "chargé"],
    5: ["très difficile", "impossible", "insane", "trop dur", "extrême", "bloquant", "enfer"]
}
# -------------------------
# Keywords for workload
# -------------------------
WORKLOAD_KEYWORDS = {
    1: ["très léger", "rien à faire", "quasi nul", "pas de travail"],
    2: ["léger", "peu de travail", "assez léger", "relax", "pas beaucoup"],
    3: ["moyen", "normal", "raisonnable"],
    4: ["beaucoup de travail", "lourd", "chargé", "intense"],
    5: ["énorme charge", "extrême", "ingérable", "insane", "trop de travail", "nuit blanche"]
}
# -------------------------
# Detection functions
# -------------------------
def detect_level(text, dictionary):
    text = text.lower()
    for level in [5, 4, 3, 2, 1]: 
        for keyword in dictionary[level]:
            if keyword in text:
                return level
    return 3  # défaut : moyen

def detect_difficulty(text):
    return detect_level(text, DIFFICULTY_KEYWORDS)

def detect_workload(text):
    return detect_level(text, WORKLOAD_KEYWORDS)

# =========================
# SEND TO API
# =========================
def send_to_api(avis_obj):
   # Mapping précis 1-5 pour la Difficulté
    diff_map = {
        1: "TRES_FACILE",
        2: "FACILE",
        3: "MOYEN",
        4: "DIFFICILE",
        5: "TRES_DIFFICILE"
    }
    
    # Mapping précis 1-5 pour la Charge
    work_map = {
        1: "TRES_LEGER",
        2: "LEGER",
        3: "MOYEN",
        4: "LOURDE",
        5: "EXTREME"
    }
    payload = {
        "coursCode": avis_obj["sigle"],
        "difficulte": diff_map.get(avis_obj["difficulty"], "MOYEN"),
        "chargeTravail": work_map.get(avis_obj["workload"], "MOYEN"),
        "commentaire": avis_obj["avis"],
        "session": "AUTOMNE",
        "annee": 2024
    }

    response = requests.post(API_URL,json=payload,headers={"Content-Type": "application/json"}
    )

    return response.status_code


# -------------------------
# Discord Client
# -------------------------
intents = discord.Intents.default()
intents.message_content = True

client = discord.Client(intents=intents)

@client.event
async def on_ready():
    print(f"[BOT] Connecté en tant que {client.user}")

@client.event
async def on_message(message):

    # Ignorer les messages du bot
    if message.author == client.user:
        return

    # Filtrer un canal spécifique
    if message.channel.id != TARGET_CHANNEL_ID:
        return

    avis_text = message.content.strip()

    # ---- CONDITION 1 : avis vide -> ne rien enregistrer ----
    if not avis_text:
        await message.channel.send("⚠️ Votre message est vide. Aucun avis enregistré.")
        return

    # ---- EXTRACTION DU SIGLE ----
    sigle_match = re.search(SIGLE_REGEX, avis_text)
    sigle = sigle_match.group(0).upper() if sigle_match else None

    # ---- CONDITION 2 : sigle absent -> ne rien enregistrer ----
    if not sigle:
        await message.channel.send(
            "⚠️ Aucun sigle de cours détecté dans votre message.\n"
            "Veuillez inclure un sigle sous la forme **IFT2255**, **LOG1000**, etc."
        )
        return

    # ---- DETECTION DIFFICULTÉ & CHARGE ----
    difficulty = detect_difficulty(avis_text)
    workload = detect_workload(avis_text)

    avis_obj = {
        "avis": avis_text,
        "sigle": sigle,
        "difficulty": difficulty,
        "workload": workload,
        "time": datetime.utcnow().isoformat()
    }
# Envoi à l'API Java
    status = send_to_api(avis_obj)
    if status != 201:
        await message.channel.send("❌ Erreur : l'avis n'a pas été accepté par l'API.")
        return

    # Confirmation Discord
    response = (
        "✅ **Avis enregistré avec succès !**\n\n"
        f"📘 **Cours** : {sigle}\n"
        f"📊 **Difficulté** : {difficulty}/5\n"
        f"📚 **Charge de travail** : {workload}/5"
    )

    await message.channel.send(response)

client.run(TOKEN)
