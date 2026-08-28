import type { Product } from "../models/product";

const BASE_URL = "/api/products";

export async function fetchProducts(searchTerm?: string): Promise<Product[]> {
    const url =
        searchTerm && searchTerm.trim().length > 0
            ? `${BASE_URL}?search=${encodeURIComponent(searchTerm.trim())}`
            : BASE_URL;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(
            `Failed to load products: HTTP ${response.status}`
        );
    }

    const data: unknown = await response.json();

    if (!isProductArray(data)) {
        throw new Error(
            "Invalid response payload format received from server."
        );
    }

    return data;
}

export async function clearProducts(): Promise<void> {
    const response = await fetch(BASE_URL, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error(
            `Failed to clear products: HTTP ${response.status}`
        );
    }
}

function isProductArray(value: unknown): value is Product[] {
    return Array.isArray(value) && value.every(isProduct);
}

function isProduct(value: unknown): value is Product {
    if (typeof value !== "object" || value === null) {
        return false;
    }

    const product = value as Record<string, unknown>;

    return (
        typeof product.store === "string" &&
        typeof product.name === "string" &&
        typeof product.price === "number" &&
        (product.previousPrice === null || typeof product.previousPrice === "number") &&
        (product.discount === null || typeof product.discount === "string") &&
        (product.sourceUrl === null || typeof product.sourceUrl === "string") &&
        (product.imageUrl === null || typeof product.imageUrl === "string")
    );
}
