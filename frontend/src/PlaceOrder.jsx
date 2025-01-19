import React, { useState, useEffect } from 'react';
import axios from 'axios';
import "./App.css";

function PlaceOrder({ kc }) {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [errors, setErrors] = useState({});
  const [quantities, setQuantities] = useState({});
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get('http://localhost:9000/api/product', {
          headers: {
            Authorization: `Bearer ${kc.token}`
          }
        });
        setProducts(response.data);
      } catch (error) {
        console.error("Error fetching products:", error);
        setMessage("Error fetching products");
      }
    };

    fetchProducts();
  }, [kc.token]);

  const handleQuantityChange = (e, productId) => {
    const value = e.target.value;
    setQuantities(prevQuantities => ({
      ...prevQuantities,
      [productId]: value,
    }));

    setErrors(prevErrors => ({
      ...prevErrors,
      [productId]: value <= 0 || isNaN(value) ? "Please enter a valid quantity (greater than 0)" : null,
    }));
  };

  const handleAddToCart = async (product) => {
    const quantity = parseInt(quantities[product.id]);

    if (quantity <= 0 || isNaN(quantity)) {
      setErrors(prevErrors => ({
        ...prevErrors,
        [product.id]: "Please enter a valid quantity (greater than 0)"
      }));
      return;
    }

    try {
      const response = await axios.get(`http://localhost:9000/api/inventory?name=${product.name}&quantity=${quantity}`, {
        headers: {
          Authorization: `Bearer ${kc.token}`
        }
      });
      if (response.data === false) {
        setErrors(prevErrors => ({
          ...prevErrors,
          [product.id]: "Not enough in stock"
        }));
      } else {
        setErrors(prevErrors => ({
          ...prevErrors,
          [product.id]: null
        }));

        setCart([...cart, { ...product, quantity }]);
        setTotalPrice(totalPrice + product.price * quantity);
        setMessage("");
      }
    } catch (error) {
      console.error("Error adding product to cart:", error);
      setMessage("Error adding product to cart");
    }
  };

  const handleRemoveFromCart = (productId) => {
    const itemToRemove = cart.find(item => item.id === productId);
    if (itemToRemove) {
      setTotalPrice(prevTotal => prevTotal - itemToRemove.price * itemToRemove.quantity);
    }

    setCart(cart.filter(item => item.id !== productId));
  };

  const handleOrderSubmit = async () => {
    const orderNumber = `ORD${Math.floor(Math.random() * 1000000)}`;
    const orderData = {
      orderNumber,
      name: "teste",
      products: cart.map(item => ({
        name: item.name,
        price: item.price,
        quantity: item.quantity
      }))
    };

    try {
      await axios.post('http://localhost:9000/api/order', orderData, {
        headers: {
          Authorization: `Bearer ${kc.token}`
        }
      });

      await Promise.all(cart.map(async (item) => {
        try {
          await axios.put(`http://localhost:9000/api/inventory/update`, null, {
            params: {
              name: item.name,
              quantity: -item.quantity
            },
            headers: {
              Authorization: `Bearer ${kc.token}`
            }
          });
        } catch (error) {
          console.error(`Error updating inventory for ${item.name}:`, error);
        }
      }));

      alert("Order placed successfully!");
      setCart([]);
      setTotalPrice(0);
    } catch (error) {
      console.error("Error placing order:", error);
      alert("Failed to place the order. Please try again.");
    }
  };

  return (
    <div className="place-order">
      <div className="product-list">
        <div className='product-grid' id='orderGrid'>
          {products.map(product => (
            <div key={product.id} className="product-card">
              <p><strong>Name:</strong> {product.name}</p>
              <p><strong>Description:</strong> {product.description}</p>
              <p><strong>Price:</strong> ${product.price}</p>
              <input 
                type="number" 
                min="1" 
                value={quantities[product.id] || ''} 
                placeholder="Quantity" 
                onChange={(e) => handleQuantityChange(e, product.id)} 
              />
              {errors[product.id] && <p className="error">{errors[product.id]}</p>}
              <button onClick={() => handleAddToCart(product)}>
                Add to Cart
              </button>
            </div>
          ))}
        </div>

        <div className="cart-summary">
          <h3>Cart Summary</h3>
          <ul>
            {cart.map((item, index) => (
              <li key={index} className="cart-item">
                {item.name} x {item.quantity} - ${item.price * item.quantity}
                <button 
                  className="remove-btn"
                  onClick={() => handleRemoveFromCart(item.id)}
                >
                  x
                </button>
              </li>
            ))}
          </ul>
          <p><strong>Total Price:</strong> ${totalPrice}</p>

          {message && <p className="message">{message}</p>}

          <button onClick={handleOrderSubmit}>Submit Order</button>
        </div>
      </div>
    </div>
  );
}

export default PlaceOrder;
