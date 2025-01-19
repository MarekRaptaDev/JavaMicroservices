import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './App.css';

function SeeAllProducts({ kc }) {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const token = kc.token;
                const response = await axios.get('http://localhost:9000/api/product', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setProducts(response.data);
            } catch (err) {
                setError('Failed to fetch products');
                console.error(err);
            }
        };

        fetchProducts();
    }, [kc]);

    return (
        <div className="product-list">
            {error && <div className="error">{error}</div>}
            {products.length > 0 ? (
                <div className="product-grid">
                    {products.map((product) => (
                        <div key={product.id} className="product-card">
                            <p><strong>Name:</strong> {product.name}</p>
                            <p><strong>Price:</strong> ${product.price}</p>
                            <p><strong>Description:</strong> {product.description}</p>
                        </div>
                    ))}
                </div>
            ) : (
                <p>Loading products...</p>
            )}
        </div>
    );
}

export default SeeAllProducts;
