

# Vehicle Maintenance React App

A full-stack vehicle maintenance tracking application built with a **React frontend** and a **backend API**.
This project helps users log and manage maintenance records for their vehicles in a simple, organized way.

I built this project essentially as the magnum opus to complete my college education. During my time at UC San Diego I took classes focused on various parts of web development from database design, backend design,
frontends, security, and networking. Now that I knew all the individual parts I felt it was time to combine them into a solo-built full stack webapp that showcases my ability to put all the requisite parts together.
This also served as a fun little playground to familiarize myself with web technologies such as Spring Boot, React, Vite, and Typescript. The app is quite simple at this point, a user can create an account and then
enter vehicle information that they want to track maintenance for as well as maintenance that has been performed on the vehicles. The app is at an MVP stage, but I have been careful to design it as modular as possible
which will allow me to continue working on it to add additional features as I have time to work on it. In the future I plan to implement optimistic updates on the frontend to allow for a more responsive frontend experience,
an AI-powered microservice to allow users to ask for tips on how to maintain their vehicles, more detailed maintenance records with the option to upload related documents, and a searching and filtering feature to allow users
to better locate the vehicles and relevant maintenance records.

## Table of Contents

* [About](#about)
* [Features](#features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)

  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running the App](#running-the-app)
* [Usage](#usage)
* [Project Structure](#project-structure)
* [Contributing](#contributing)
* [License](#license)

---

## About

VehicleMaintenanceReactApp is a web application that allows users to:

* Add and manage vehicles
* Log maintenance activities (oil changes, inspections, repairs, etc.)
* Track history and upcoming service reminders

It’s designed to be a **simple, extensible platform** for tracking all maintenance data in one place.

---

## Features

- Create, read, update, and delete (CRUD) vehicles
- Log maintenance records with details and dates
- Filter and view maintenance history
- Responsive UI built with React
- RESTful backend API for data persistence

---

## 🛠 Tech Stack

| Layer    | Technology                                       |
| -------- | ------------------------------------------------ |
| Frontend | React, TypeScript, CSS                           |
| Backend  | Node.js, Express (or similar — adjust as needed) |
| Data     | JSON or database (configure as needed)           |
| Build    | npm / yarn                                       |

---

## Getting Started

### Prerequisites

Make sure you have the following installed:

```bash
# Node.js and npm
node --version
npm --version
```

Clone the repository:

```bash
git clone https://github.com/thedonutdan/VehicleMaintenanceReactApp.git
cd VehicleMaintenanceReactApp
```

---

### Installation

#### Frontend

```bash
cd frontend
npm install
```

#### Backend

```bash
cd backend
npm install
```

---

### Running the App

#### Start Backend Server

```bash
cd backend
npm run start
```

*(Adjust script name if backend uses a different command like `dev`.)*

#### Start Frontend App

```bash
cd frontend
npm run start
```

The React app will open in your browser (usually at `http://localhost:3000`).
The backend API will run on its configured port (e.g., `http://localhost:5000`).

---

## Project Structure

```
VehicleMaintenanceReactApp/
├── backend/          # Server API code
├── frontend/         # React application
├── .gitignore
└── README.md
```

---

## Contributing

Contributions are welcome! Whether it’s a bug fix, feature, or documentation improvement:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/xyz`)
3. Commit your changes
4. Open a Pull Request

---

## License

Distributed under the MIT License.
