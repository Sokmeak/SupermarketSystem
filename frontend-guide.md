# Frontend Integration Guide

## API Base URL

```javascript
const API_BASE_URL = "http://localhost:8080/api";
```

## Authentication

### 1. Login

```javascript
// POST /api/login
const login = async (username, password) => {
  const response = await fetch(`${API_BASE_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  const data = await response.json();
  // Store token
  localStorage.setItem("token", data.token);
  localStorage.setItem("user", JSON.stringify(data.user));
  return data;
};
```

### 2. API Calls with Token

```javascript
const apiCall = async (endpoint, options = {}) => {
  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    // Token expired or invalid
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
  }

  return response.json();
};
```

## Dashboard API

### Get Summary Statistics

```javascript
// GET /api/reports/summary (requires authentication)
const getDashboardSummary = async () => {
  return await apiCall("/reports/summary");
};

// Response:
// {
//   "totalItems": 15,
//   "lowStock": 5,
//   "stockValue": 1234.56
// }
```

## Products API (Paginated)

### Get All Products

```javascript
// GET /api/products?page=0&size=10&sortBy=name&sortDir=asc&keyword=&categoryId=
const getProducts = async (
  page = 0,
  size = 10,
  sortBy = "name",
  sortDir = "asc",
  keyword = "",
  categoryId = null
) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    sortBy,
    sortDir,
    ...(keyword && { keyword }),
    ...(categoryId && { categoryId: categoryId.toString() }),
  });

  return await apiCall(`/products?${params}`);
};

// Response:
// {
//   "content": [...],          // Array of products
//   "pageable": {...},
//   "totalPages": 2,
//   "totalElements": 15,
//   "size": 10,
//   "number": 0,              // Current page
//   "first": true,
//   "last": false,
//   "numberOfElements": 10
// }
```

### Get Single Product

```javascript
// GET /api/products/{code}
const getProduct = async (code) => {
  return await apiCall(`/products/${code}`);
};
```

### Create Product

```javascript
// POST /api/products (multipart/form-data)
const createProduct = async (productData) => {
  const formData = new FormData();
  formData.append("productCode", productData.code);
  formData.append("name", productData.name);
  formData.append("description", productData.description);
  formData.append("price", productData.price);
  formData.append("categoryId", productData.categoryId);
  formData.append("image", productData.imageFile); // File object

  const token = localStorage.getItem("token");
  const response = await fetch(`${API_BASE_URL}/products`, {
    method: "POST",
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: formData,
  });

  return response.text();
};
```

### Update Product

```javascript
// PUT /api/products/{code} (multipart/form-data)
const updateProduct = async (code, productData) => {
  const formData = new FormData();
  formData.append("name", productData.name);
  formData.append("description", productData.description);
  formData.append("price", productData.price);
  formData.append("categoryId", productData.categoryId);
  if (productData.imageFile) {
    formData.append("image", productData.imageFile);
  }

  const token = localStorage.getItem("token");
  const response = await fetch(`${API_BASE_URL}/products/${code}`, {
    method: "PUT",
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: formData,
  });

  return response.text();
};
```

### Delete Product

```javascript
// DELETE /api/products/{code}
const deleteProduct = async (code) => {
  return await apiCall(`/products/${code}`, { method: "DELETE" });
};
```

## Categories API (Paginated)

### Get All Categories

```javascript
// GET /api/categories?page=0&size=10&sortBy=name&sortDir=asc
const getCategories = async (
  page = 0,
  size = 10,
  sortBy = "name",
  sortDir = "asc"
) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    sortBy,
    sortDir,
  });

  return await apiCall(`/categories?${params}`);
};
```

### Create Category

```javascript
// POST /api/categories
const createCategory = async (name, description) => {
  return await apiCall("/categories", {
    method: "POST",
    body: JSON.stringify({ name, description }),
  });
};
```

### Update Category

```javascript
// PUT /api/categories/{id}
const updateCategory = async (id, name, description) => {
  return await apiCall(`/categories/${id}`, {
    method: "PUT",
    body: JSON.stringify({ name, description }),
  });
};
```

### Delete Category

```javascript
// DELETE /api/categories/{id}
const deleteCategory = async (id) => {
  return await apiCall(`/categories/${id}`, { method: "DELETE" });
};
```

## User Management API (Admin only)

### Get All Users

```javascript
// GET /api/admin/users
const getUsers = async () => {
  return await apiCall("/admin/users");
};
```

### Create User

```javascript
// POST /api/admin/users
const createUser = async (userData) => {
  return await apiCall("/admin/users", {
    method: "POST",
    body: JSON.stringify({
      username: userData.username,
      email: userData.email,
      password: userData.password,
      role: userData.role, // "ADMIN" or "USER"
    }),
  });
};
```

### Update User

```javascript
// POST /api/admin/users/{id}
const updateUser = async (id, userData) => {
  return await apiCall(`/admin/users/${id}`, {
    method: "POST",
    body: JSON.stringify({
      username: userData.username,
      email: userData.email,
      role: userData.role,
      active: userData.active,
    }),
  });
};
```

## Image URLs

Product images are served at:

```javascript
const getImageUrl = (imagePath) => {
  // imagePath from API: "/images/filename.jpg"
  return `${API_BASE_URL}${imagePath}`;
};
```

## Vue 3 Composition API Examples

### Dashboard Component

```vue
<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">Dashboard</h1>

    <div class="grid grid-cols-3 gap-4">
      <div class="bg-white p-4 shadow rounded">
        Total Items: {{ summary.totalItems }}
      </div>
      <div class="bg-white p-4 shadow rounded">
        Low Stock: {{ summary.lowStock }}
      </div>
      <div class="bg-white p-4 shadow rounded">
        Stock Value: ${{ summary.stockValue }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { apiCall } from "@/utils/api";

const summary = ref({
  totalItems: 0,
  lowStock: 0,
  stockValue: 0,
});

onMounted(async () => {
  try {
    const data = await apiCall("/reports/summary");
    summary.value = data;
  } catch (error) {
    console.error("Failed to load dashboard:", error);
  }
});
</script>
```

### Products List Component

```vue
<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">Products</h1>

    <!-- Search and Filter -->
    <div class="mb-4 flex gap-4">
      <input
        v-model="searchKeyword"
        @input="loadProducts"
        placeholder="Search products..."
        class="border px-4 py-2 rounded"
      />
      <select
        v-model="selectedCategory"
        @change="loadProducts"
        class="border px-4 py-2 rounded"
      >
        <option :value="null">All Categories</option>
        <option v-for="cat in categories" :key="cat.id" :value="cat.id">
          {{ cat.name }}
        </option>
      </select>
    </div>

    <!-- Products Grid -->
    <div class="grid grid-cols-4 gap-4 mb-4">
      <div
        v-for="product in products"
        :key="product.id"
        class="border rounded p-4"
      >
        <img
          :src="`http://localhost:8080/api${product.image}`"
          :alt="product.name"
          class="w-full h-48 object-cover mb-2"
        />
        <h3 class="font-bold">{{ product.name }}</h3>
        <p class="text-sm text-gray-600">{{ product.category.name }}</p>
        <p class="text-lg font-bold mt-2">${{ product.price }}</p>
      </div>
    </div>

    <!-- Pagination -->
    <div class="flex justify-center gap-2">
      <button
        @click="changePage(page - 1)"
        :disabled="page === 0"
        class="px-4 py-2 border rounded disabled:opacity-50"
      >
        Previous
      </button>
      <span class="px-4 py-2"> Page {{ page + 1 }} of {{ totalPages }} </span>
      <button
        @click="changePage(page + 1)"
        :disabled="page >= totalPages - 1"
        class="px-4 py-2 border rounded disabled:opacity-50"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { apiCall } from "@/utils/api";

const products = ref([]);
const categories = ref([]);
const searchKeyword = ref("");
const selectedCategory = ref(null);
const page = ref(0);
const pageSize = ref(12);
const totalPages = ref(0);

const loadProducts = async () => {
  const params = new URLSearchParams({
    page: page.value.toString(),
    size: pageSize.value.toString(),
    sortBy: "name",
    sortDir: "asc",
    ...(searchKeyword.value && { keyword: searchKeyword.value }),
    ...(selectedCategory.value && {
      categoryId: selectedCategory.value.toString(),
    }),
  });

  const data = await apiCall(`/products?${params}`);
  products.value = data.content;
  totalPages.value = data.totalPages;
};

const changePage = (newPage) => {
  page.value = newPage;
  loadProducts();
};

const loadCategories = async () => {
  const data = await apiCall("/categories?size=100");
  categories.value = data.content;
};

onMounted(() => {
  loadCategories();
  loadProducts();
});
</script>
```

### Login Component

```vue
<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-md w-96">
      <h1 class="text-2xl font-bold mb-6">Login</h1>

      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block mb-2">Username</label>
          <input
            v-model="username"
            type="text"
            class="w-full border px-4 py-2 rounded"
            required
          />
        </div>

        <div class="mb-6">
          <label class="block mb-2">Password</label>
          <input
            v-model="password"
            type="password"
            class="w-full border px-4 py-2 rounded"
            required
          />
        </div>

        <button
          type="submit"
          class="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
        >
          Login
        </button>

        <p v-if="error" class="text-red-500 mt-4">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const username = ref("");
const password = ref("");
const error = ref("");

const handleLogin = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
      }),
    });

    const data = await response.json();

    if (response.ok) {
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data.user));
      router.push("/dashboard");
    } else {
      error.value = data.message || "Login failed";
    }
  } catch (err) {
    error.value = "Connection error";
  }
};
</script>
```

## API Utility File (utils/api.js)

```javascript
const API_BASE_URL = "http://localhost:8080/api";

export const apiCall = async (endpoint, options = {}) => {
  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
    throw new Error("Unauthorized");
  }

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Request failed");
  }

  return response.json();
};

export const apiCallText = async (endpoint, options = {}) => {
  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Request failed");
  }

  return response.text();
};
```

## Environment Variables (.env)

```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

## Notes

- All protected routes require `Authorization: Bearer {token}` header
- Token expires after 24 hours (configured in backend)
- CORS is configured to allow `http://localhost:*` and `http://127.0.0.1:*`
- Image files are served from `/api/images/{filename}`
- Pagination starts at page 0
- Default page size is 10 items
