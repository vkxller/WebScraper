import "./style.css";

import {
    generateProductCardHtml
} from "./components/ProductCard.ts";

import type { Product } from "./models/product.ts";

enum CatalogState {
    IDLE = "IDLE",
    LOADING = "LOADING",
    SUCCESS = "SUCCESS",
    EMPTY = "EMPTY",
    ERROR = "ERROR"
}

let products: Product[] = [];

async function loadCatalog(searchTerm?: string): Promise<void> {

    const container =
        document.getElementById(
            "contenedor-catalogo"
        );

    const errorBlock =
        document.getElementById(
            "bloque-error"
        );

    if (
        container === null
        || errorBlock === null
    ) {
        return;
    }

    setCatalogState(
        container,
        errorBlock,
        CatalogState.LOADING
    );

    const loadingText =
        searchTerm && searchTerm.length > 0
            ? `Buscando "${escapeHtml(searchTerm)}" en Falabella...`
            : "Cargando productos desde Falabella...";

    container.innerHTML = `<p>${loadingText}</p>`;
    errorBlock.textContent = "";

    try {
        const url =
            searchTerm && searchTerm.length > 0
                ? `/api/products?search=${encodeURIComponent(searchTerm)}`
                : "/api/products";

        const response =
            await fetch(url);

        if (!response.ok) {
            throw new Error(
                `Error del servidor: código HTTP ${response.status}`
            );
        }

        const data: unknown =
            await response.json();

        if (!isProductArray(data)) {
            throw new Error(
                "La respuesta del servidor no tiene un formato válido."
            );
        }

        products = data;

        renderProducts(
            products,
            container,
            errorBlock,
            searchTerm
        );

    } catch (error: unknown) {

        console.error(
            "Fallo al obtener los productos:",
            error
        );

        setCatalogState(
            container,
            errorBlock,
            CatalogState.ERROR
        );

        const message =
            error instanceof Error
                ? error.message
                : "Error desconocido.";

        errorBlock.textContent =
            `No fue posible obtener los productos. ${message}`;

        container.innerHTML = "";
    }
}

function renderProducts(
    productsToRender: Product[],
    container: HTMLElement,
    errorBlock: HTMLElement,
    searchTerm?: string
): void {

    errorBlock.textContent = "";

    if (productsToRender.length === 0) {

        setCatalogState(
            container,
            errorBlock,
            CatalogState.EMPTY
        );

        const emptyMessage =
            searchTerm && searchTerm.length > 0
                ? `No se encontraron productos para "${escapeHtml(searchTerm)}" en Falabella.`
                : "No hay productos disponibles en este momento.";

        container.innerHTML = `<p>${emptyMessage}</p>`;

        return;
    }

    setCatalogState(
        container,
        errorBlock,
        CatalogState.SUCCESS
    );

    container.innerHTML =
        productsToRender
            .map(generateProductCardHtml)
            .join("");
}

function setupSearchForm(): void {

    const productForm =
        document.getElementById(
            "form-productos"
        ) as HTMLFormElement | null;

    if (productForm === null) {
        return;
    }

    productForm.addEventListener(
        "submit",
        (event: Event) => {

            event.preventDefault();

            const searchInput =
                document.getElementById(
                    "txt-busqueda"
                ) as HTMLInputElement | null;

            if (searchInput === null) {
                return;
            }

            const searchValue =
                searchInput.value
                    .trim();

            void loadCatalog(searchValue);
        }
    );
}

function setCatalogState(
    container: HTMLElement,
    errorBlock: HTMLElement,
    state: CatalogState
): void {

    container.dataset.state = state;
    errorBlock.dataset.state = state;
}

function isProductArray(
    value: unknown
): value is Product[] {

    return Array.isArray(value)
        && value.every(isProduct);
}

function isProduct(
    value: unknown
): value is Product {

    if (
        typeof value !== "object"
        || value === null
    ) {
        return false;
    }

    const product =
        value as Record<string, unknown>;

    return (
        typeof product.store === "string"
        && typeof product.name === "string"
        && typeof product.price === "number"

        && (
            product.previousPrice === null
            || typeof product.previousPrice === "number"
        )

        && (
            product.discount === null
            || typeof product.discount === "string"
        )

        && (
            product.sourceUrl === null
            || typeof product.sourceUrl === "string"
        )

        && (
            product.imageUrl === null
            || typeof product.imageUrl === "string"
        )
    );
}

function escapeHtml(value: string): string {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

document.addEventListener(
    "DOMContentLoaded",
    () => {
        setupSearchForm();
        void loadCatalog();
    }
);