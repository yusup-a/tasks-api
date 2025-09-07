const API = "/api";
let token = null;

function setStatus(msg, isError = false) {
  const el = document.getElementById("status");
  el.textContent = msg;
  el.className = isError ? "status error" : "status";
}

// Register
document.getElementById("registerForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("regUser").value;
  const password = document.getElementById("regPass").value;
  try {
    const res = await fetch(`${API}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    if (res.ok) {
      setStatus("✅ Registered successfully! Please login.");
    } else {
      setStatus("❌ Registration failed", true);
    }
  } catch {
    setStatus("❌ Network error during registration", true);
  }
});

// Login
document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("loginUser").value;
  const password = document.getElementById("loginPass").value;
  try {
    const res = await fetch(`${API}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    if (res.ok) {
      const data = await res.json();
      token = data.token;
      setStatus(`✅ Logged in as ${username}`);
      document.getElementById("tasksSection").style.display = "block";
    } else {
      setStatus("❌ Login failed", true);
    }
  } catch {
    setStatus("❌ Network error during login", true);
  }
});

// Add task
document.getElementById("addTaskForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const text = document.getElementById("taskText").value;
  try {
    const res = await fetch(`${API}/tasks`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ text }),
    });
    if (res.ok) {
      setStatus("✅ Task added!");
      document.getElementById("taskText").value = "";
      loadTasks();
    } else {
      setStatus("❌ Failed to add task", true);
    }
  } catch {
    setStatus("❌ Network error while adding task", true);
  }
});

// Refresh tasks
document.getElementById("refreshBtn").addEventListener("click", loadTasks);

async function loadTasks() {
  try {
    const res = await fetch(`${API}/tasks`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (res.ok) {
      const tasks = await res.json();
      const list = document.getElementById("tasks");
      list.innerHTML = "";
      tasks.forEach((t) => {
        const li = document.createElement("li");
        li.textContent = t.text;
        list.appendChild(li);
      });
    } else {
      setStatus("❌ Failed to load tasks", true);
    }
  } catch {
    setStatus("❌ Network error while loading tasks", true);
  }
}
