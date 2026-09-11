# 📰 News App

A modern Android News application that displays the **latest news** with pagination and offline support.

The app is built with modern Android development technologies and follows a structured architecture to provide a smooth user experience, efficient network loading, image loading, and offline support.

## ✨ Features

* 📰 Display latest news
* 📄 **Pagination** — load news page by page
* 🔄 Pull to refresh
* 🌐 Load news from a remote API
* 🖼️ Load and display news images
* 📱 Modern Android UI
* 🧭 Navigation between screens
* 💾 Offline news storage
* ⚡ Efficient data loading
* 🛜 Network error handling

## 🛠️ Tech Stack

| Technology          | Purpose                          |
| ------------------- | -------------------------------- |
| **Kotlin**          | Programming language             |
| **Jetpack Compose** | UI development                   |
| **Ktor**            | Networking / API requests        |
| **Koin**            | Dependency Injection             |
| **Coil**            | Image loading                    |
| **Navigation**      | Screen navigation                |
| **Room**            | Local database / Offline support |
| **Pagination**      | Load news data page by page      |

## 📄 Pagination

The News App loads articles incrementally instead of downloading all news at once.

```text
┌───────────────┐
│   News API    │
└───────┬───────┘
        │
        │ Page 1
        ▼
┌───────────────┐
│   News List   │
└───────┬───────┘
        │
        │ User scrolls
        ▼
     Page 2
        │
        ▼
     Page 3
        │
        ▼
     Page 4
        │
        ▼
       ...
```

### Pagination Flow

```text
User scrolls to bottom
        │
        ▼
Request next page
        │
        ▼
      Ktor
        │
        ▼
    News API
        │
        ▼
New articles received
        │
        ▼
Update Room Database
        │
        ▼
Update UI
```

This approach helps reduce the amount of data loaded at one time and provides a smoother browsing experience.

## 💾 Offline Support

**Room Database** is used to cache news articles locally.

The application can display previously loaded news when the device has no internet connection.

```text
                 ┌─────────────┐
                 │  News API   │
                 └──────┬──────┘
                        │
                      Ktor
                        │
                        ▼
                  ┌───────────┐
                  │ Repository│
                  └─────┬─────┘
                        │
             ┌──────────┴──────────┐
             ▼                     ▼
      ┌─────────────┐       ┌─────────────┐
      │    Room     │       │     UI      │
      │  Database   │       │   Compose   │
      └─────────────┘       └─────────────┘
```

### Offline Flow

```text
Internet Available
       │
       ▼
Fetch News
       │
       ▼
Save to Room
       │
       ▼
Display News


No Internet
       │
       ▼
Read from Room
       │
       ▼
Display Cached News
```

## 🌐 Networking

The application uses **Ktor Client** to communicate with the News API.

Example request:

```text
GET /news?page=1&pageSize=20
```

When the user reaches the end of the current list:

```text
page = 1
   ↓
page = 2
   ↓
page = 3
   ↓
page = 4
   ↓
...
```

## 🖼️ Image Loading

**Coil** is used to efficiently load and display news images from remote URLs.

```kotlin
AsyncImage(
    model = article.imageUrl,
    contentDescription = article.title
)
```

## 🧭 Navigation

The application uses **Jetpack Navigation** to navigate between screens.

```text
Home Screen
     │
     ├── Select News
     │
     ▼
News Detail Screen
```

## 💉 Dependency Injection

**Koin** is used for Dependency Injection.

Dependencies include:

* Ktor Client
* Room Database
* DAO
* Repository
* ViewModel

## 🏗️ Architecture

```text
UI
│
├── Jetpack Compose
├── ViewModel
│
▼
Repository
│
├── Remote Data Source
│   └── Ktor
│
└── Local Data Source
    └── Room
```

## 📂 Project Structure

```text
com.example.newsapp
│
├── data
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   └── entity
│   │
│   ├── remote
│   │   ├── api
│   │   └── dto
│   │
│   └── repository
│
├── di
│   └── AppModule
│
├── navigation
│   └── NavGraph
│
├── presentation
│   ├── screen
│   │   ├── HomeScreen
│   │   └── DetailScreen
│   │
│   └── viewmodel
│
└── MainActivity.kt
```

# 📸 Screenshots

<div align="center">

| Home Screen (Light Theme)                       | Detail Screen (Light Theme)                   |
| ----------------------------------------------- | --------------------------------------------- |
| <img width="1280" height="2856" alt="Screenshot_20260911_131420" src="https://github.com/user-attachments/assets/30a46d69-708a-41d6-9082-3fcd1b95d486"/> | <img width="1280" height="2856" alt="Screenshot_20260911_131534" src="https://github.com/user-attachments/assets/0666909c-407d-4216-aee1-a34ed12e69b8" /> |

| Home Screen (Dark Theme)                          | Detail Screen (Dark Theme)                            |
| ------------------------------------------------- | ----------------------------------------------------- |
| <img width="1280" height="2856" alt="Screenshot_20260911_131452" src="https://github.com/user-attachments/assets/342ded84-21de-4e8f-885c-c7ccfcbc0eb4" />
| <img width="1280" height="2856" alt="Screenshot_20260911_131509" src="https://github.com/user-attachments/assets/dea275fa-074e-4277-8cbf-2aeb4f0e2380" />|

</div>

## 📚 What I Learned

Through this project, I practiced:

* Kotlin
* Jetpack Compose
* Ktor Client
* Koin Dependency Injection
* Room Database
* **Pagination**
* Offline-first concepts
* Repository pattern
* Navigation
* Image loading with Coil
* API integration
* Android application architecture
