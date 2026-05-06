# PlantCare AI — Android App

An AI-powered Android app that analyzes plant photos to provide identification, health assessment, and personalized care recommendations using GPT-4o vision and a RAG-backed knowledge base.

---

## Prerequisites

- Android Studio (latest stable)
- Android device or emulator (API 24+)
- The [plant-care-backend](https://github.com/droid0291/plant-care-backend) server **must be running** before testing the app
- Both your development machine and Android device must be on the **same Wi-Fi network**

---

## Step 1 — Start the Backend Server

> The app will not work without the backend running. Complete these steps first.

### 1.1 Clone the backend repository

```bash
git clone https://github.com/droid0291/plant-care-backend.git
cd plant-care-backend
```

### 1.2 Create and activate a virtual environment

```bash
python3 -m venv .venv
source .venv/bin/activate        # macOS/Linux
# .venv\Scripts\activate         # Windows
```

### 1.3 Install dependencies

```bash
pip install -r requirements.txt
```

### 1.4 Configure environment variables

```bash
cp .env.example .env
```

Open `.env` and set your OpenAI API key:

```
OPENAI_API_KEY=sk-proj-your-key-here
```

### 1.5 Build the RAG knowledge base (one-time setup)

```bash
python3 rag/setup_knowledge_base.py
```

### 1.6 Start the server

```bash
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

The server starts at `http://0.0.0.0:8000`. To confirm it is running, visit:

```
http://127.0.0.1:8000/health
```

Expected response: `{"status": "ok", "service": "PlantCare AI"}`

### 1.7 Find your machine's local IP address

The Android app needs your machine's LAN IP to reach the server. Run:

```bash
ipconfig getifaddr en0       # macOS
# hostname -I                # Linux
```

Example output: `10.0.0.226`

The app is pre-configured to use `http://10.0.0.226:8000`. If your IP differs, update `BASE_URL` in:

```
app/src/main/java/com/plantcare/app/di/NetworkModule.kt
```

---

## Step 2 — Run the Android App

### 2.1 Open the project

Open this `android/` folder in Android Studio.

### 2.2 Sync Gradle

Android Studio will prompt you to sync Gradle dependencies automatically. Click **Sync Now** if prompted.

### 2.3 Connect your device

- **Physical device:** Enable USB Debugging in Developer Options and connect via USB. Make sure the device is on the same Wi-Fi network as the backend machine.
- **Emulator:** Use an emulator with API 24+ (note: camera features require a physical device for real photos).

### 2.4 Build and run

Click the **Run** button (▶) or press `Shift + F10`.

---

## Features

- **Plant Identification** — Common name, scientific name, family, and confidence score
- **Health Assessment** — Status indicator with urgency levels (Healthy / Needs Attention / Act Soon / Critical)
- **Sunlight Requirements** — Light level, daily hours, and care tips
- **Water Requirements** — Frequency, amount, and watering tips
- **Issue Detection** — Lists any detected problems with the plant
- **Improvement Tips & Care Tips** — Actionable recommendations
- **Fun Facts** — Interesting facts about the identified plant
- **RAG Sources** — Displays the knowledge base sources used for the analysis

---

## Architecture

The app follows **MVVM with Clean Architecture**:

```
UI Layer        →  Jetpack Compose screens + ViewModels
Data Layer      →  Repository → Retrofit API service → Backend
DI              →  Hilt
Image Loading   →  Coil
Serialization   →  kotlinx.serialization
```

**Data flow:**
1. User captures or selects a plant photo
2. Image is compressed to max 1024px and Base64-encoded
3. `HomeViewModel` sends the image to `PlantRepository`
4. Repository calls `POST /api/v1/analyze` on the backend
5. Backend returns a structured analysis response
6. Results are mapped to domain models and displayed in `ResultScreen`

---

## API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Health check |
| POST | `/api/v1/analyze` | Analyze a plant image |

**Request body (`/api/v1/analyze`):**

```json
{
  "image_base64": "<base64-encoded JPEG>",
  "user_note": "Optional note about the plant"
}
```

---

## Permissions

| Permission | Purpose |
|------------|---------|
| `INTERNET` | Backend API communication |
| `CAMERA` | Capture plant photos |
| `READ_MEDIA_IMAGES` | Select photos from gallery |

---

## Tech Stack

| Category | Library / Tool |
|----------|----------------|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Compose Navigation |
| Networking | Retrofit 2.11 + OkHttp 4.12 |
| Serialization | kotlinx.serialization 1.7 |
| DI | Hilt 2.51 |
| Image Loading | Coil 2.7 |
| Async | Kotlin Coroutines 1.8 |
| Min SDK | 24 |
| Target SDK | 34 |

---

## Troubleshooting

**App shows a network error**
- Confirm the backend server is running (`/health` endpoint returns OK)
- Verify your device and machine are on the same Wi-Fi network
- Double-check the `BASE_URL` in `NetworkModule.kt` matches your machine's LAN IP

**Analysis takes a long time**
- The backend calls GPT-4o which can take up to 30 seconds. The app timeout is set to 60 seconds.

**Camera not working on emulator**
- Use a physical device for camera capture, or use the gallery option with a pre-existing image.