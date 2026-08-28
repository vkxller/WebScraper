import "./style.css";

import { generateProductCardHtml } from "./components/ProductCard.ts";
import { fetchProducts } from "./services/productApi.ts";
import type { Product } from "./models/product.ts";

enum CatalogState {
    IDLE = "IDLE",
    LOADING = "LOADING",
    SUCCESS = "SUCCESS",
    EMPTY = "EMPTY",
    ERROR = "ERROR",
}

let products: Product[] = [];

async function loadCatalog(searchTerm?: string): Promise<void> {
    const container = document.getElementById("contenedor-catalogo");
    const errorBlock = document.getElementById("bloque-error");

    if (container === null || errorBlock === null) {
        return;
    }

    setCatalogState(container, errorBlock, CatalogState.LOADING);

    const loadingText =
        searchTerm && searchTerm.length > 0
            ? `Buscando "${escapeHtml(searchTerm)}" en Falabella...`
            : "Cargando productos desde Falabella...";

    container.innerHTML = `<p>${loadingText}</p>`;
    errorBlock.textContent = "";

    try {
        products = await fetchProducts(searchTerm);

        renderProducts(products, container, errorBlock, searchTerm);
    } catch (error: unknown) {
        console.error("Fallo al obtener los productos:", error);

        setCatalogState(container, errorBlock, CatalogState.ERROR);

        const message =
            error instanceof Error ? error.message : "Error desconocido.";

        errorBlock.textContent = `No fue posible obtener los productos. ${message}`;
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
        setCatalogState(container, errorBlock, CatalogState.EMPTY);

        const emptyMessage =
            searchTerm && searchTerm.length > 0
                ? `No se encontraron productos para "${escapeHtml(searchTerm)}" en Falabella.`
                : "No hay productos disponibles en este momento.";

        container.innerHTML = `<p>${emptyMessage}</p>`;
        return;
    }

    setCatalogState(container, errorBlock, CatalogState.SUCCESS);

    container.innerHTML = productsToRender
        .map(generateProductCardHtml)
        .join("");
}

function setupSearchForm(): void {
    const productForm = document.getElementById(
        "form-productos"
    ) as HTMLFormElement | null;

    if (productForm === null) {
        return;
    }

    productForm.addEventListener("submit", (event: Event) => {
        event.preventDefault();

        const searchInput = document.getElementById(
            "txt-busqueda"
        ) as HTMLInputElement | null;

        if (searchInput === null) {
            return;
        }

        const searchValue = searchInput.value.trim();

        void loadCatalog(searchValue);
    });
}

function setCatalogState(
    container: HTMLElement,
    errorBlock: HTMLElement,
    state: CatalogState
): void {
    container.dataset.state = state;
    errorBlock.dataset.state = state;
}

function escapeHtml(value: string): string {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

document.addEventListener("DOMContentLoaded", () => {
    setupSearchForm();
    void loadCatalog();
});