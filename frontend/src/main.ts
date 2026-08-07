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

async function loadCatalog(): Promise<void> {

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

    container.innerHTML =
        "<p>Cargando productos desde el servidor...</p>";

    try {

        const response =
            await fetch("/api/products");

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
            errorBlock
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
    errorBlock: HTMLElement
): void {

    errorBlock.textContent = "";

    if (productsToRender.length === 0) {

        setCatalogState(
            container,
            errorBlock,
            CatalogState.EMPTY
        );

        container.innerHTML =
            "<p>No hay productos disponibles en este momento.</p>";

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

            const container =
                document.getElementById(
                    "contenedor-catalogo"
                );

            const errorBlock =
                document.getElementById(
                    "bloque-error"
                );

            if (
                searchInput === null
                || container === null
                || errorBlock === null
            ) {
                return;
            }

            const searchValue =
                searchInput.value
                    .trim()
                    .toLowerCase();

            if (searchValue.length === 0) {

                renderProducts(
                    products,
                    container,
                    errorBlock
                );

                return;
            }

            const filteredProducts =
                products.filter(
                    (product) =>
                        product.name
                            .toLowerCase()
                            .includes(searchValue)
                );

            renderProducts(
                filteredProducts,
                container,
                errorBlock
            );
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
    );
}

document.addEventListener(
    "DOMContentLoaded",
    () => {
        setupSearchForm();
        void loadCatalog();
    }
);