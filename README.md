# 📐 Mathematical Structures Visualizer

A beginner-friendly Java + Swing + SQLite desktop application that lets you
draw, calculate, and save mathematical shapes.

---

## 🗂️ Project Structure

```
MathVisualizer/
├── src/
│   ├── Main.java             ← Entry point (run this)
│   ├── MainGUI.java          ← Main application window
│   ├── DatabaseManager.java  ← SQLite database logic
│   ├── Shape.java            ← Abstract base class
│   ├── CircleShape.java      ← Circle logic + drawing
│   ├── SquareShape.java      ← Square logic + drawing
│   ├── RectangleShape.java   ← Rectangle logic + drawing
│   ├── ConeShape.java        ← Cone logic + drawing
│   ├── DrawPanel.java        ← Custom canvas (JPanel)
│   └── RecordsPanel.java     ← Database records table
├── lib/
│   └── sqlite-jdbc-*.jar     ← ⬅ YOU MUST DOWNLOAD THIS (see Step 4)
├── bin/                      ← Created automatically after compilation
├── compile_run.bat           ← Windows: double-click to build & run
├── compile_run.sh            ← Mac/Linux: run in terminal
└── README.md                 ← This file
```

---

## 🛠️ COMPLETE SETUP GUIDE (Beginner-Friendly)

### Step 1 – Install Java JDK 17+

1. Go to: https://adoptium.net/  (free, recommended)
2. Click **Latest LTS Release** → Download the installer for your OS
3. Run the installer. ✅ Check "Set JAVA_HOME" if the option appears
4. Verify installation — open a terminal / Command Prompt and type:
   ```
   java -version
   ```
   You should see something like: `openjdk 17.0.x ...`

> **Windows users**: If `java` is not found, you need to set environment variables:
> 1. Search "Environment Variables" in Start Menu
> 2. Under **System Variables** → find `Path` → click Edit
> 3. Add a new entry: `C:\Program Files\Eclipse Adoptium\jdk-17...\bin`
>    (adjust path to match your installed JDK location)

---

### Step 2 – Install Visual Studio Code

1. Go to: https://code.visualstudio.com/
2. Download and install VS Code for your OS
3. Open VS Code

---

### Step 3 – Install Java Extensions in VS Code

Inside VS Code:
1. Press `Ctrl+Shift+X` to open Extensions
2. Search for: **Extension Pack for Java**
3. Click **Install** (this installs all needed Java tools)
4. Restart VS Code when prompted

---

### Step 4 – Download the SQLite JDBC Driver

The SQLite driver lets Java talk to the database.

1. Go to: https://github.com/xerial/sqlite-jdbc/releases
2. Download the latest file named `sqlite-jdbc-X.X.X.jar`
3. Place it inside the `lib/` folder of this project

> Example: `MathVisualizer/lib/sqlite-jdbc-3.45.1.0.jar`

---

### Step 5 – Open the Project in VS Code

1. Open VS Code
2. Go to **File → Open Folder...**
3. Select the `MathVisualizer` folder
4. You'll see all the files in the Explorer panel on the left

---

### Step 6 – Compile & Run

**Windows:**
- Double-click `compile_run.bat`
- OR open a terminal in VS Code (`Ctrl+\``) and type:
  ```cmd
  javac -cp "lib\*" -d bin src\*.java
  java  -cp "lib\*;bin" Main
  ```

**Mac / Linux:**
```bash
chmod +x compile_run.sh    # Run this ONCE to make it executable
./compile_run.sh
```

**Or manually in the VS Code terminal:**
```bash
javac -cp "lib/*" -d bin src/*.java
java  -cp "lib/*:bin" Main
```

---

## 🎯 How to Use the App

1. **Select a shape** from the dropdown (Circle, Square, Rectangle, Cone)
2. **Enter dimensions** in the input fields that appear
3. Click **▶ Draw Shape** to see the shape on the canvas
4. Click **💾 Save to Database** to save the record
5. Switch to **📋 View Records** tab to see all saved shapes
6. Use the **🗑 Delete Selected** button to remove a record

---

## 🗃️ Database Info

- Database file: `shapes.db` (created automatically on first run)
- Table: `shapes`

| Column      | Type     | Description                          |
|-------------|----------|--------------------------------------|
| id          | INTEGER  | Auto-incremented unique ID           |
| shape_type  | TEXT     | "Circle", "Square", etc.             |
| dimensions  | TEXT     | e.g. "radius=5.0"                    |
| area        | REAL     | Calculated area (decimal number)     |
| perimeter   | REAL     | Calculated perimeter                 |
| created_at  | DATETIME | Timestamp, auto-set by SQLite        |

To manually inspect the database, install **DB Browser for SQLite**:
https://sqlitebrowser.org/

---

## 🔧 Troubleshooting

| Problem | Fix |
|---------|-----|
| `java: command not found` | JDK not installed or not in PATH |
| `error: cannot find symbol` | Missing import or wrong class name |
| `No suitable driver found` | sqlite-jdbc JAR is not in `lib/` |
| Window doesn't open | Check terminal for error messages |
| DB not saving | Ensure `lib/` folder contains the SQLite JAR |
