import { useEffect, useState } from "react";
import { apiRequest } from "./api";

const initialCart = { items: [], subtotal: 0, totalItems: 0 };

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("ecommerce_token") || "");
  const [user, setUser] = useState(null);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [cart, setCart] = useState(initialCart);
  const [orders, setOrders] = useState([]);
  const [adminOrders, setAdminOrders] = useState([]);
  const [filters, setFilters] = useState({ keyword: "", categoryId: "" });
  const [loginForm, setLoginForm] = useState({ email: "customer@shop.local", password: "Customer@123" });
  const [registerForm, setRegisterForm] = useState({ fullName: "", email: "", password: "" });
  const [checkoutForm, setCheckoutForm] = useState({
    shippingAddress: "221B Baker Street, London",
    paymentMethod: "CARD",
    notes: "Testing order only - do not fulfill"
  });
  const [categoryForm, setCategoryForm] = useState({ name: "", description: "" });
  const [productForm, setProductForm] = useState({
    name: "",
    sku: "",
    description: "",
    price: "",
    stockQuantity: "",
    imageUrl: "",
    categoryId: ""
  });
  const [message, setMessage] = useState("Sandbox mode active. This storefront is only for testing seeded API flows.");
  const [mode, setMode] = useState("login");
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    localStorage.setItem("ecommerce_token", token);
  }, [token]);

  useEffect(() => {
    loadCatalog();
  }, [filters.keyword, filters.categoryId]);

  useEffect(() => {
    if (!token) {
      setUser(null);
      setCart(initialCart);
      setOrders([]);
      setAdminOrders([]);
      return;
    }
    loadSession(token);
  }, [token]);

  async function loadCatalog() {
    try {
      const params = new URLSearchParams({ size: "24", sortBy: "createdAt", direction: "DESC" });
      if (filters.keyword.trim()) params.set("keyword", filters.keyword.trim());
      if (filters.categoryId) params.set("categoryId", filters.categoryId);

      const [productResult, categoryResult] = await Promise.all([
        apiRequest(`/products?${params.toString()}`),
        apiRequest("/categories")
      ]);
      setProducts(productResult.content || []);
      setCategories(categoryResult || []);
    } catch (error) {
      setMessage(readError(error));
    }
  }

  async function loadSession(activeToken) {
    try {
      const profile = await apiRequest("/users/me", { token: activeToken });
      setUser(profile);

      const requests = [apiRequest("/cart", { token: activeToken }), apiRequest("/orders/mine", { token: activeToken })];
      if (profile.role === "ADMIN") {
        requests.push(apiRequest("/orders/admin", { token: activeToken }));
      }
      const [cartResult, orderResult, adminResult] = await Promise.all(requests);
      setCart(cartResult);
      setOrders(orderResult);
      setAdminOrders(adminResult || []);
      setMessage(`Sandbox session ready for ${profile.fullName} (${profile.role}).`);
    } catch (error) {
      setToken("");
      setMessage(readError(error));
    }
  }

  async function login(event) {
    event.preventDefault();
    await authenticate("/auth/login", loginForm);
  }

  async function register(event) {
    event.preventDefault();
    await authenticate("/auth/register", registerForm);
  }

  async function authenticate(path, payload) {
    setBusy(true);
    try {
      const response = await apiRequest(path, { method: "POST", body: payload });
      setToken(response.token);
      setUser(response.user);
      setMessage("Authentication successful. You are inside the test storefront.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function addToCart(productId) {
    if (!token) {
      setMessage("Sign in as USER first to add products.");
      return;
    }
    setBusy(true);
    try {
      const result = await apiRequest("/cart/items", {
        method: "POST",
        token,
        body: { productId, quantity: 1 }
      });
      setCart(result);
      setMessage("Item added to the sandbox cart.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function updateQuantity(itemId, quantity) {
    setBusy(true);
    try {
      if (quantity <= 0) {
        await apiRequest(`/cart/items/${itemId}`, { method: "DELETE", token });
        setCart(await apiRequest("/cart", { token }));
      } else {
        setCart(
          await apiRequest(`/cart/items/${itemId}`, {
            method: "PUT",
            token,
            body: { quantity }
          })
        );
      }
      setMessage("Cart updated inside the test environment.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function checkout(event) {
    event.preventDefault();
    setBusy(true);
    try {
      const order = await apiRequest("/orders/checkout", { method: "POST", token, body: checkoutForm });
      setCart(await apiRequest("/cart", { token }));
      setOrders(await apiRequest("/orders/mine", { token }));
      setMessage(`Test order ${order.orderNumber} placed successfully.`);
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function createCategory(event) {
    event.preventDefault();
    setBusy(true);
    try {
      await apiRequest("/categories", { method: "POST", token, body: categoryForm });
      setCategoryForm({ name: "", description: "" });
      await loadCatalog();
      setMessage("Category added to the sandbox catalog.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function createProduct(event) {
    event.preventDefault();
    setBusy(true);
    try {
      await apiRequest("/products", {
        method: "POST",
        token,
        body: {
          ...productForm,
          price: Number(productForm.price),
          stockQuantity: Number(productForm.stockQuantity),
          categoryId: Number(productForm.categoryId),
          active: true
        }
      });
      setProductForm({ name: "", sku: "", description: "", price: "", stockQuantity: "", imageUrl: "", categoryId: "" });
      await loadCatalog();
      setMessage("Product added to the demo inventory.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  async function markOrderPaid(orderId) {
    setBusy(true);
    try {
      await apiRequest(`/orders/admin/${orderId}`, {
        method: "PATCH",
        token,
        body: { status: "CONFIRMED", paymentStatus: "PAID" }
      });
      setAdminOrders(await apiRequest("/orders/admin", { token }));
      setMessage("Order moved to CONFIRMED / PAID in the sandbox.");
    } catch (error) {
      setMessage(readError(error));
    } finally {
      setBusy(false);
    }
  }

  function logout() {
    localStorage.removeItem("ecommerce_token");
    setToken("");
    setMessage("Signed out. Public catalog mode is still available for testing.");
  }

  const inventoryValue = products.reduce(
    (sum, product) => sum + Number(product.price) * Number(product.stockQuantity),
    0
  );
  const activeCategory = categories.find((category) => String(category.id) === filters.categoryId);

  return (
    <div className="shell">
      <div className="orb orb-one" />
      <div className="orb orb-two" />
      <div className="page">
      <header className="hero">
        <div className="hero-copy">
          <p className="eyebrow">Testing sandbox only</p>
          <h1>API validation workspace for catalog and checkout flows.</h1>
          <p className="lead">
            Use this demo surface to validate login, catalog filtering, cart updates, checkout, and admin workflows.
            Every credential, product, and order on this screen is test data for development only.
          </p>
          <div className="warning-banner">
            <span className="warning-pill">Sandbox Mode</span>
            <p>{message}</p>
          </div>
          <div className="stat-grid">
            <article className="stat-card">
              <strong>{products.length}</strong>
              <span>demo products</span>
            </article>
            <article className="stat-card">
              <strong>{categories.length}</strong>
              <span>test categories</span>
            </article>
            <article className="stat-card">
              <strong>Rs {inventoryValue.toFixed(0)}</strong>
              <span>seeded inventory value</span>
            </article>
            <article className="stat-card">
              <strong>{orders.length + adminOrders.length}</strong>
              <span>orders in session</span>
            </article>
          </div>
        </div>
        <div className="hero-stack">
          <div className="hero-card credential-card">
            <strong>Demo Credentials</strong>
            <div className="credential-row">
              <span>USER</span>
              <p>customer@shop.local</p>
              <small>Customer@123</small>
            </div>
            <div className="credential-row">
              <span>ADMIN</span>
              <p>admin@shop.local</p>
              <small>Admin@123</small>
            </div>
          </div>
          <div className="hero-card">
            <strong>Current Session</strong>
            <p>{user ? `${user.fullName} (${user.role})` : "Anonymous browsing mode is active."}</p>
            {user ? <button onClick={logout}>Log out</button> : null}
          </div>
        </div>
      </header>

      <section className="grid">
        <aside className="panel sticky-panel">
          <div className="panel-head">
            <p className="eyebrow">Access</p>
            <strong>{user ? `${user.fullName} (${user.role})` : "Enter the testing sandbox"}</strong>
          </div>

          {!user ? (
            <>
              <div className="tabs">
                <button className={mode === "login" ? "active" : ""} onClick={() => setMode("login")} type="button">Login</button>
                <button className={mode === "register" ? "active" : ""} onClick={() => setMode("register")} type="button">Register</button>
              </div>
              {mode === "login" ? (
                <form className="form" onSubmit={login}>
                  <input value={loginForm.email} onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })} placeholder="Email" />
                  <input type="password" value={loginForm.password} onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })} placeholder="Password" />
                  <button disabled={busy}>Sign in</button>
                </form>
              ) : (
                <form className="form" onSubmit={register}>
                  <input value={registerForm.fullName} onChange={(e) => setRegisterForm({ ...registerForm, fullName: e.target.value })} placeholder="Full name" />
                  <input value={registerForm.email} onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })} placeholder="Email" />
                  <input type="password" value={registerForm.password} onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })} placeholder="Password" />
                  <button disabled={busy}>Create account</button>
                </form>
              )}
            </>
          ) : (
            <div className="profile-card">
              <strong>{user.fullName}</strong>
              <p>{user.email}</p>
              <span className="pill">{user.role}</span>
              <small>This account is part of the seeded demo environment.</small>
            </div>
          )}
        </aside>

        <section className="panel wide catalog-panel">
          <div className="panel-head">
            <p className="eyebrow">Catalog Sandbox</p>
            <strong>{products.length} products ready for testing</strong>
          </div>
          <div className="filters">
            <input value={filters.keyword} onChange={(e) => setFilters({ ...filters, keyword: e.target.value })} placeholder="Search products" />
            <select value={filters.categoryId} onChange={(e) => setFilters({ ...filters, categoryId: e.target.value })}>
              <option value="">All categories</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>{category.name}</option>
              ))}
            </select>
          </div>
          <div className="category-ribbon">
            {categories.slice(0, 10).map((category) => (
              <button
                key={category.id}
                type="button"
                className={filters.categoryId === String(category.id) ? "chip active-chip" : "chip"}
                onClick={() =>
                  setFilters({
                    ...filters,
                    categoryId: filters.categoryId === String(category.id) ? "" : String(category.id)
                  })
                }
              >
                {category.name}
              </button>
            ))}
          </div>
          <div className="catalog">
            {products.length ? (
              products.map((product) => (
                <article className="card product-card" key={product.id}>
                  <div className="product-meta-top">
                    <small>{product.categoryName}</small>
                    <small>{product.sku}</small>
                  </div>
                  <h3>{product.name}</h3>
                  <p>{product.description}</p>
                  <div className="row product-row">
                    <div>
                      <strong>Rs {Number(product.price).toFixed(2)}</strong>
                      <span>{product.stockQuantity} in stock</span>
                    </div>
                    <button onClick={() => addToCart(product.id)} type="button">Add</button>
                  </div>
                </article>
              ))
            ) : (
              <div className="empty-catalog">
                <strong>No products found</strong>
                <p>
                  {activeCategory
                    ? `There are no visible products for ${activeCategory.name} in the current database.`
                    : "No products match the current search and filter combination."}
                </p>
                <button type="button" className="secondary-button" onClick={() => setFilters({ keyword: "", categoryId: "" })}>
                  Clear filters
                </button>
              </div>
            )}
          </div>
        </section>

        <div className="rail-stack sticky-panel">
          <aside className="panel">
            <div className="panel-head">
              <p className="eyebrow">Checkout Test</p>
              <strong>{cart.totalItems || 0} items in sandbox cart</strong>
            </div>
            <div className="list">
              {cart.items.length ? cart.items.map((item) => (
                <div className="list-item" key={item.id}>
                  <div>
                    <strong>{item.productName}</strong>
                    <p>{item.quantity} x Rs {Number(item.unitPrice).toFixed(2)}</p>
                  </div>
                  <div className="quantity">
                    <button type="button" onClick={() => updateQuantity(item.id, item.quantity - 1)}>-</button>
                    <span>{item.quantity}</span>
                    <button type="button" onClick={() => updateQuantity(item.id, item.quantity + 1)}>+</button>
                  </div>
                </div>
              )) : <p className="muted">Cart is empty. Add products to simulate checkout behavior.</p>}
            </div>
            <div className="subtotal">
              <span>Sandbox subtotal</span>
              <strong>Rs {Number(cart.subtotal || 0).toFixed(2)}</strong>
            </div>
            <form className="form" onSubmit={checkout}>
              <textarea rows="3" value={checkoutForm.shippingAddress} onChange={(e) => setCheckoutForm({ ...checkoutForm, shippingAddress: e.target.value })} placeholder="Shipping address" />
              <select value={checkoutForm.paymentMethod} onChange={(e) => setCheckoutForm({ ...checkoutForm, paymentMethod: e.target.value })}>
                <option value="CARD">CARD</option>
                <option value="UPI">UPI</option>
                <option value="COD">COD</option>
              </select>
              <textarea rows="2" value={checkoutForm.notes} onChange={(e) => setCheckoutForm({ ...checkoutForm, notes: e.target.value })} placeholder="Notes" />
              <button disabled={!token || busy || !cart.items.length}>Place test order</button>
            </form>
          </aside>

          <aside className="panel">
            <div className="panel-head">
              <p className="eyebrow">Order Log</p>
              <strong>{orders.length} test orders</strong>
            </div>
            <div className="list">
              {orders.length ? orders.map((order) => (
                <div className="list-item" key={order.id}>
                  <div>
                    <strong>{order.orderNumber}</strong>
                    <p>{order.status} - {order.paymentStatus}</p>
                  </div>
                  <strong>Rs {Number(order.totalAmount).toFixed(2)}</strong>
                </div>
              )) : <p className="muted">No orders yet. Place a test order to verify checkout and history.</p>}
            </div>
          </aside>
        </div>
      </section>

      {user && user.role === "ADMIN" ? (
        <section className="secondary-grid">
          <section className="panel">
            <div className="panel-head">
              <p className="eyebrow">Admin Sandbox</p>
              <strong>Create category</strong>
            </div>
            <form className="form" onSubmit={createCategory}>
              <input value={categoryForm.name} onChange={(e) => setCategoryForm({ ...categoryForm, name: e.target.value })} placeholder="Category name" />
              <textarea rows="3" value={categoryForm.description} onChange={(e) => setCategoryForm({ ...categoryForm, description: e.target.value })} placeholder="Description" />
              <button disabled={busy}>Save category</button>
            </form>
          </section>

          <section className="panel">
            <div className="panel-head">
              <p className="eyebrow">Admin Sandbox</p>
              <strong>Create product</strong>
            </div>
            <form className="form" onSubmit={createProduct}>
              <input value={productForm.name} onChange={(e) => setProductForm({ ...productForm, name: e.target.value })} placeholder="Product name" />
              <input value={productForm.sku} onChange={(e) => setProductForm({ ...productForm, sku: e.target.value })} placeholder="SKU" />
              <textarea rows="3" value={productForm.description} onChange={(e) => setProductForm({ ...productForm, description: e.target.value })} placeholder="Description" />
              <div className="pair">
                <input type="number" step="0.01" value={productForm.price} onChange={(e) => setProductForm({ ...productForm, price: e.target.value })} placeholder="Price" />
                <input type="number" value={productForm.stockQuantity} onChange={(e) => setProductForm({ ...productForm, stockQuantity: e.target.value })} placeholder="Stock" />
              </div>
              <input value={productForm.imageUrl} onChange={(e) => setProductForm({ ...productForm, imageUrl: e.target.value })} placeholder="Image URL" />
              <select value={productForm.categoryId} onChange={(e) => setProductForm({ ...productForm, categoryId: e.target.value })}>
                <option value="">Select category</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>{category.name}</option>
                ))}
              </select>
              <button disabled={busy}>Save product</button>
            </form>
          </section>

          <section className="panel">
            <div className="panel-head">
              <p className="eyebrow">Admin Queue</p>
              <strong>{adminOrders.length} test orders queued</strong>
            </div>
            <div className="list">
              {adminOrders.length ? adminOrders.map((order) => (
                <div className="list-item stacked" key={order.id}>
                  <div>
                    <strong>{order.orderNumber}</strong>
                    <p>{order.customerName} - {order.status}</p>
                  </div>
                  <button type="button" onClick={() => markOrderPaid(order.id)}>Confirm and mark paid</button>
                </div>
              )) : <p className="muted">No admin orders waiting for review.</p>}
            </div>
          </section>
        </section>
      ) : null}
      </div>
    </div>
  );
}

function readError(error) {
  const payload = error.payload || {};
  const validation = payload.errors ? ` ${Object.values(payload.errors).join(" ")}` : "";
  return `${payload.detail || error.message || "Request failed."}${validation}`;
}

export default App;
