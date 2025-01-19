import React, { useState } from 'react';
import axios from 'axios';
import "./App.css";

function AddNewProduct({ kc }) {
  const [productData, setProductData] = useState({
    name: '',
    description: '',
    price: '',
    quantity: ''
  });
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProductData({
      ...productData,
      [name]: value
    });
  };

  const validateForm = () => {
    let formErrors = {};
    if (!productData.name) formErrors.name = "Name is required";
    if (!productData.description) formErrors.description = "Description is required";
    if (!productData.price || isNaN(productData.price) || productData.price <= 0) formErrors.price = "Price must be a positive number";
    if (!productData.quantity || isNaN(productData.quantity) || productData.quantity <= 0) formErrors.quantity = "Quantity must be a positive number";
    setErrors(formErrors);
    return Object.keys(formErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      const productResponse = await axios.post('http://localhost:9000/api/product', {
        name: productData.name,
        description: productData.description,
        price: parseFloat(productData.price)
      }, {
        headers: {
          Authorization: `Bearer ${kc.token}`
        }
      });

      if (productResponse.status === 201) {
        const inventoryResponse = await axios.post(`http://localhost:9000/api/inventory/add`, null, {
          params: {
            name: productData.name,
            quantity: productData.quantity
          },
          headers: {
            Authorization: `Bearer ${kc.token}`
          }
        });

        if (inventoryResponse.status === 201) {
          setMessage(`Item has been added`);
          setProductData({ name: '', description: '', price: '', quantity: '' });
          setErrors({});
        }
      }
    } catch (error) {
      if (error.response) {
        setMessage(`Error: ${error.response.data.message || "An error occurred"}`);
      } else {
        setMessage("Error: Unable to connect to the server.");
      }
    }
  };

  return (
    <div className="add-new-product">
      <h3>Add New Product</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input 
            type="text" 
            name="name" 
            value={productData.name} 
            onChange={handleChange} 
          />
          {errors.name && <p className="error">{errors.name}</p>}
        </div>

        <div>
          <label>Description:</label>
          <input 
            type="text" 
            name="description" 
            value={productData.description} 
            onChange={handleChange} 
          />
          {errors.description && <p className="error">{errors.description}</p>}
        </div>

        <div>
          <label>Price:</label>
          <input 
            type="number" 
            name="price" 
            value={productData.price} 
            onChange={handleChange} 
          />
          {errors.price && <p className="error">{errors.price}</p>}
        </div>

        <div>
          <label>Quantity:</label>
          <input 
            type="number" 
            name="quantity" 
            value={productData.quantity} 
            onChange={handleChange} 
          />
          {errors.quantity && <p className="error">{errors.quantity}</p>}
        </div>

        <button type="submit">Add Product</button>
      </form>

      {message && <p className={message.includes("Error") ? "error" : "success"}>{message}</p>}
    </div>
  );
}

export default AddNewProduct;
