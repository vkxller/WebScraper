<title>WebScraper - Documentation</title>

<style>
    * {
        box-sizing: border-box;
    }

    body {
        margin: 0;
        padding: 0;
        background: #f6f8fa;
        color: #24292f;
        font-family:
            -apple-system,
            BlinkMacSystemFont,
            "Segoe UI",
            Helvetica,
            Arial,
            sans-serif;
        line-height: 1.6;
    }

    .container {
        width: min(1100px, calc(100% - 40px));
        margin: 40px auto;
    }

    header {
        background: #ffffff;
        border: 1px solid #d0d7de;
        border-radius: 12px;
        padding: 40px;
        margin-bottom: 24px;
    }

    h1 {
        margin-top: 0;
        margin-bottom: 8px;
        font-size: 36px;
    }

    h2 {
        margin-top: 0;
        padding-bottom: 10px;
        border-bottom: 1px solid #d8dee4;
    }

    h3 {
        margin-top: 28px;
    }

    section {
        background: #ffffff;
        border: 1px solid #d0d7de;
        border-radius: 12px;
        padding: 30px;
        margin-bottom: 24px;
    }

    .subtitle {
        color: #57606a;
        font-size: 20px;
        margin-top: 0;
    }

    .description {
        color: #57606a;
        max-width: 850px;
    }

    ul {
        padding-left: 25px;
    }

    li {
        margin-bottom: 6px;
    }

    code {
        background: #f6f8fa;
        border: 1px solid #d8dee4;
        border-radius: 5px;
        padding: 2px 6px;
        font-family:
            Consolas,
            "Courier New",
            monospace;
        font-size: 0.9em;
    }

    pre {
        background: #0d1117;
        color: #e6edf3;
        padding: 20px;
        border-radius: 8px;
        overflow-x: auto;
        border: 1px solid #30363d;
    }

    pre code {
        background: transparent;
        border: none;
        padding: 0;
        color: inherit;
        font-size: 14px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
    }

    th,
    td {
        border: 1px solid #d0d7de;
        padding: 10px 12px;
        text-align: left;
    }

    th {
        background: #f6f8fa;
    }

    .status {
        text-align: center;
        font-weight: 600;
    }

    .architecture {
        overflow-x: auto;
    }

    .note {
        background: #f6f8fa;
        border-left: 4px solid #57606a;
        padding: 14px 18px;
        margin: 20px 0;
        border-radius: 4px;
    }

    .success {
        color: #1a7f37;
        font-weight: 600;
    }

    .future {
        color: #57606a;
    }

    .grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 16px;
        margin-top: 20px;
    }

    .card {
        border: 1px solid #d0d7de;
        border-radius: 8px;
        padding: 20px;
        background: #ffffff;
    }

    .card h3 {
        margin-top: 0;
    }

    footer {
        text-align: center;
        color: #57606a;
        padding: 20px;
    }

    @media (max-width: 700px) {
        .container {
            width: min(100% - 20px, 1100px);
            margin: 20px auto;
        }

        header,
        section {
            padding: 20px;
        }

        h1 {
            font-size: 28px;
        }

        table {
            font-size: 14px;
        }
    }
</style>

<!-- HEADER -->

<header>
    <h1>🛒 WebScraper</h1>

    <p class="subtitle">
        Java web scraper with TypeScript frontend built with Clean Architecture
    </p>

    <p class="description">
        A maintainable and testable application for extracting, processing
        and displaying e-commerce product information.
    </p>

    <p class="description">
        The project currently consists of a Java scraping application and a
        TypeScript web frontend that consumes the scraped product information
        through an HTTP API.
    </p>
</header>


<!-- OVERVIEW -->

<section>
    <h2>📖 Overview</h2>

    <p>
        <strong>WebScraper</strong> is a Java application designed to extract
        product information from the Falabella Chile website.
    </p>

    <p>
        The project follows <strong>Clean Architecture</strong> principles,
        keeping business logic independent from external libraries and
        infrastructure components. This results in a solution that is
        maintainable, testable and prepared for future expansion.
    </p>

    <p>The current implementation extracts:</p>

    <ul>
        <li>Store name</li>
        <li>Product name</li>
        <li>Current price</li>
        <li>Previous price, when available</li>
        <li>Discount, when available</li>
        <li>Source URL, when available</li>
    </ul>

    <p>
        The project also includes a web frontend developed with
        <strong>TypeScript and Vite</strong>, allowing the scraped products
        to be displayed and filtered through a browser.
    </p>

    <p>
        The long-term objective is to evolve the project into a complete
        product and price comparison platform.
    </p>
</section>


<!-- FEATURES -->

<section>
    <h2>✨ Features</h2>

    <div class="grid">

        <div class="card">
            <h3>Scraper</h3>

            <ul>
                <li>Extract product information from Falabella Chile.</li>
                <li>Extract current prices.</li>
                <li>Extract previous prices when available.</li>
                <li>Extract discount information.</li>
                <li>Extract product source URLs when available.</li>
                <li>Validate product information at the domain level.</li>
                <li>Separate scraping, parsing and application logic.</li>
                <li>Handle optional product information.</li>
            </ul>
        </div>

        <div class="card">
            <h3>Web frontend</h3>

            <ul>
                <li>Display scraped products.</li>
                <li>Search products by name.</li>
                <li>Load products asynchronously.</li>
                <li>Display loading feedback.</li>
                <li>Display empty states.</li>
                <li>Display API errors.</li>
                <li>Use TypeScript strict typing.</li>
                <li>Validate API responses.</li>
            </ul>
        </div>

        <div class="card">
            <h3>API</h3>

            <ul>
                <li>Expose scraped products through HTTP.</li>
                <li>Reuse the existing scraper service.</li>
                <li>Return product data as JSON.</li>
                <li>Keep scraper logic independent from the frontend.</li>
                <li>Support local frontend requests.</li>
            </ul>
        </div>

    </div>
</section>


<!-- TECHNOLOGIES -->

<section>
    <h2>🛠️ Technologies</h2>

    <table>
        <thead>
            <tr>
                <th>Technology</th>
                <th>Purpose</th>
            </tr>
        </thead>

        <tbody>
            <tr>
                <td>Java 21</td>
                <td>Main programming language</td>
            </tr>

            <tr>
                <td>Maven</td>
                <td>Build and dependency management</td>
            </tr>

            <tr>
                <td>Jsoup</td>
                <td>HTML download and parsing</td>
            </tr>

            <tr>
                <td>JUnit 5</td>
                <td>Unit testing</td>
            </tr>

            <tr>
                <td>Mockito</td>
                <td>Test doubles and dependency isolation</td>
            </tr>

            <tr>
                <td>JaCoCo</td>
                <td>Code coverage reports</td>
            </tr>

            <tr>
                <td>TypeScript</td>
                <td>Frontend programming language</td>
            </tr>

            <tr>
                <td>Vite</td>
                <td>Frontend development server and build tool</td>
            </tr>

            <tr>
                <td>HTML5</td>
                <td>Web interface structure</td>
            </tr>

            <tr>
                <td>CSS</td>
                <td>Frontend styling</td>
            </tr>

            <tr>
                <td>Git</td>
                <td>Version control</td>
            </tr>

            <tr>
                <td>GitHub</td>
                <td>Source code hosting</td>
            </tr>
        </tbody>
    </table>
</section>


<!-- PROJECT STRUCTURE -->

<section>
    <h2>📁 Project Structure</h2>

</section>


<!-- ARCHITECTURE -->

<section>
    <h2>🏛️ Architecture</h2>

    <h3>Java scraper</h3>

    <h3>Dependency flow</h3>

    <p>
        The domain layer does not depend on Jsoup, frameworks or
        infrastructure implementations.
    </p>

    <h3>Web application flow</h3>

    <p>
        The API reuses the existing
        <code>ProductScraperService</code>, avoiding duplication of
        the scraper logic.
    </p>
</section>


<!-- FRONTEND -->

<section>
    <h2>🌐 Frontend</h2>

    <p>
        The frontend was implemented using
        <strong>TypeScript Vanilla and Vite</strong>.
    </p>

    <p>
        The frontend is intentionally kept simple at this stage.
        The current focus is functionality, type safety, DOM manipulation
        and communication with the Java backend.
    </p>

    <h3>Main frontend components</h3>

    <h4><code>product.ts</code></h4>

    <p>
        Defines the structure expected for a product received from the API.
    </p>

    <h4><code>ProductCard.ts</code></h4>

    <p>
        Contains the logic used to generate the HTML representation
        of each product. This keeps product rendering separated from
        the main application logic.
    </p>

    <h4><code>main.ts</code></h4>

    <p>Handles:</p>

    <ul>
        <li>API requests</li>
        <li>Loading states</li>
        <li>Error handling</li>
        <li>Product validation</li>
        <li>Product rendering</li>
        <li>Search functionality</li>
        <li>Form submission</li>
        <li>DOM interaction</li>
    </ul>

    <p>
        The application uses TypeScript strict mode and avoids using
        <code>any</code> for API data.
    </p>
</section>


<!-- SEARCH -->

<section>
    <h2>🔎 Product Search</h2>

    <p>
        The frontend includes a simple search form.
    </p>

    <p>
        The search is performed over the products already loaded from the API.
    </p>

    <p>
        The frontend filters products whose names contain the searched text.
    </p>

    <p>
        The form uses <code>event.preventDefault()</code> to prevent
        the browser from reloading the page during the search.
    </p>
</section>


<!-- API -->

<section>
    <h2>🔄 Asynchronous API Consumption</h2>

    <p>
        Products are requested asynchronously using
        <code>fetch</code>, <code>async/await</code> and
        <code>try/catch</code>.
    </p>

    <p>
        The frontend verifies the HTTP response before processing the data.
    </p>

    <p>
        The API response is also validated before being used by the
        application. This prevents unexpected data structures from being
        directly rendered into the interface.
    </p>
</section>


<!-- STATES -->

<section>
    <h2>⚠️ Frontend States</h2>

    <h3>Loading</h3>

    <h3>Success</h3>

    <p>
        The available products are displayed.
    </p>

    <h3>Empty</h3>

    <h3>Error</h3>

    <p>
        If the API cannot be reached or returns an invalid response,
        the frontend displays an error message instead of failing silently.
    </p>
</section>


<!-- BUSINESS RULES -->

<section>
    <h2>📜 Business Rules</h2>

    <ol>
        <li>Every product must have a valid store name.</li>
        <li>Every product must have a valid product name.</li>
        <li>Every product must have a valid current price.</li>
        <li>The current price cannot be negative.</li>
        <li>The previous price is optional.</li>
        <li>The previous price cannot be negative when present.</li>
        <li>Discount information is optional.</li>
        <li>The product source URL is optional.</li>
        <li>Invalid products are rejected through domain validation.</li>
        <li>Business rules must remain independent from infrastructure components.</li>
    </ol>

    <p>
        These rules are centralized in the domain model instead of being
        distributed across parsers or clients.
    </p>
</section>


<!-- DESIGN DECISIONS -->

<section>
    <h2>🧠 Design Decisions</h2>

    <h3>Clean Architecture</h3>

    <p>
        Clean Architecture was selected to keep the project independent
        from specific frameworks and technical implementations.
    </p>

    <p>
        This allows infrastructure components to be replaced without
        modifying business logic.
    </p>

    <h3>Ports and Adapters</h3>

    <p>The application defines ports such as:</p>

    <ul>
        <li><code>HtmlClient</code></li>
        <li><code>ProductParser</code></li>
    </ul>

    <p>Their implementations are located in the infrastructure layer:</p>

    <ul>
        <li><code>JsoupHtmlClient</code></li>
        <li><code>FalabellaProductParser</code></li>
    </ul>

    <h3>Constructor Injection</h3>

    <p>
        Dependencies are provided through constructors.
        This avoids hidden dependencies and simplifies unit testing.
    </p>

    <h3>Domain Validation</h3>

    <p>
        Product validation is performed when domain objects are created.
        This guarantees that an invalid <code>Product</code> cannot exist
        inside the application.
    </p>

    <h3>Optional Product Information</h3>

    <p>
        Some Falabella products do not contain previous price,
        discount or product URL information in the initial HTML response.
        For that reason, these fields are optional.
    </p>

    <h3>Immutable Results</h3>

    <p>
        The application service returns product collections that cannot
        be modified externally. This protects the scraper result and
        reduces unintended state changes.
    </p>

    <h3>Independent Web Adapter</h3>

    <p>
        The web API was added without changing the existing scraping logic.
        Its purpose is to expose the existing application functionality
        to the frontend while keeping the scraper independent from the
        presentation layer.
    </p>
</section>


<!-- PRINCIPLES -->

<section>
    <h2>🧩 Architecture Principles</h2>

    <ul>
        <li>Clean Architecture</li>
        <li>Separation of Concerns</li>
        <li>Dependency Inversion Principle</li>
        <li>Single Responsibility Principle</li>
        <li>Open/Closed Principle</li>
        <li>Low coupling</li>
        <li>High cohesion</li>
        <li>Explicit dependencies</li>
        <li>Testability by design</li>
        <li>Maintainability</li>
        <li>Extensibility</li>
    </ul>
</section>


<!-- GETTING STARTED -->

<section>
    <h2>🚀 Getting Started</h2>

    <h3>Prerequisites</h3>

    <p>Make sure the following tools are installed:</p>

    <p>Verify the installations:</p>

</section>


<!-- JAVA -->

<section>
    <h2>☕ Running the Java Application</h2>

    <p>From the project root:</p>

    <h3>Run all tests</h3>

    <h3>Validate the complete Java project</h3>

    <h3>Generate the JaCoCo report</h3>

    <p>The generated report can be found at:</p>

    <h3>Run the original scraper</h3>

</section>


<!-- WEB APP -->

<section>
    <h2>🌐 Running the Web Application</h2>

    <p>
        The web application requires both the Java API and the Vite
        development server to be running.
    </p>

    <h3>1. Start the Java API</h3>

    <p>From the project root:</p>

    <p>The API starts at:</p>

    <p>The products endpoint is:</p>

    <h3>2. Install frontend dependencies</h3>

    <p>Open another terminal and enter the frontend directory:</p>

    <p>Install the dependencies:</p>

    <h3>3. Start the frontend</h3>

    <p>From the <code>frontend</code> directory:</p>

    <p>Vite will normally start the development server at:</p>

    <p>
        Open that address in a browser to access the web application.
    </p>
</section>


<!-- BUILD -->

<section>
    <h2>🔨 Build the Frontend</h2>

    <p>
        To verify the TypeScript project and generate a production build:
    </p>

    <p>
        The build performs TypeScript compilation and then generates the
        Vite production bundle.
    </p>

    <h3>Preview the production build</h3>

</section>


<!-- COMMUNICATION -->

<section>
    <h2>🔗 Frontend and API Communication</h2>

    <p>
        During development, Vite proxies requests beginning with
        <code>/api</code> to the Java API.
    </p>

    <p>The frontend uses:</p>

    <p>
        instead of hardcoding the backend URL in the application code.
    </p>

    <p>
        The development proxy is configured in:
    </p>

    <p>
        with the Java API running on:
    </p>

</section>


<!-- TESTING -->

<section>
    <h2>🧪 Testing</h2>

    <p>
        The project includes unit tests for the main components and behaviors.
    </p>

    <table>
        <thead>
            <tr>
                <th>Test area</th>
                <th>Validations</th>
            </tr>
        </thead>

        <tbody>
            <tr>
                <td>Domain model</td>
                <td>Required attributes and invalid values</td>
            </tr>

            <tr>
                <td>Product equality</td>
                <td><code>equals()</code> and <code>hashCode()</code> behavior</td>
            </tr>

            <tr>
                <td>Application service</td>
                <td>Downloading, parsing and returning products</td>
            </tr>

            <tr>
                <td>Product parser</td>
                <td>HTML extraction and optional attributes</td>
            </tr>

            <tr>
                <td>HTTP client</td>
                <td>HTML retrieval and error handling</td>
            </tr>

            <tr>
                <td>Domain exceptions</td>
                <td>Validation failure behavior</td>
            </tr>
        </tbody>
    </table>

    <p>
        JUnit 5 is used as the testing framework, while Mockito isolates
        dependencies in application-level tests.
    </p>
</section>


<!-- CODE QUALITY -->

<section>
    <h2>📊 Code Quality</h2>

    <p>The project emphasizes code quality through:</p>

    <ul>
        <li>Unit tests with JUnit 5</li>
        <li>Dependency isolation with Mockito</li>
        <li>Code coverage reports with JaCoCo</li>
        <li>Complete domain validation coverage</li>
        <li>Full branch coverage in the domain layer</li>
        <li>Constructor dependency injection</li>
        <li>Consistent package organization</li>
        <li>Descriptive class and method names</li>
        <li>Separation between business and infrastructure logic</li>
        <li>Automated Maven verification</li>
        <li>Strict TypeScript compilation</li>
        <li>Typed frontend models</li>
        <li>Explicit API response validation</li>
        <li>Error handling for asynchronous requests</li>
    </ul>

    <h3>Current domain coverage</h3>

    <table>
        <thead>
            <tr>
                <th>Layer</th>
                <th>Instruction Coverage</th>
                <th>Branch Coverage</th>
            </tr>
        </thead>

        <tbody>
            <tr>
                <td>Domain Model</td>
                <td class="status">100%</td>
                <td class="status">100%</td>
            </tr>

            <tr>
                <td>Domain Exceptions</td>
                <td class="status">100%</td>
                <td class="status">100%</td>
            </tr>
        </tbody>
    </table>
</section>


<!-- MILESTONE 1 -->

<section>
    <h2>🎯 Milestone 1</h2>

    <table>
        <thead>
            <tr>
                <th>Requirement</th>
                <th>Status</th>
            </tr>
        </thead>

        <tbody>
            <tr><td>Java 21</td><td class="status">✅</td></tr>
            <tr><td>Maven project</td><td class="status">✅</td></tr>
            <tr><td>Jsoup integration</td><td class="status">✅</td></tr>
            <tr><td>Product extraction</td><td class="status">✅</td></tr>
            <tr><td>Clean Architecture</td><td class="status">✅</td></tr>
            <tr><td>Ports and adapters</td><td class="status">✅</td></tr>
            <tr><td>Domain validation</td><td class="status">✅</td></tr>
            <tr><td>Unit tests</td><td class="status">✅</td></tr>
            <tr><td>JUnit 5</td><td class="status">✅</td></tr>
            <tr><td>Mockito</td><td class="status">✅</td></tr>
            <tr><td>JaCoCo</td><td class="status">✅</td></tr>
            <tr><td>Domain coverage</td><td class="status">✅ 100%</td></tr>
            <tr><td>Domain branch coverage</td><td class="status">✅ 100%</td></tr>
        </tbody>
    </table>
</section>


<!-- MILESTONE 2 -->

<section>
    <h2>🎯 Milestone 2</h2>

    <p>
        The second milestone extends the original scraper with a web
        interface and asynchronous communication with the Java application.
    </p>

    <table>
        <thead>
            <tr>
                <th>Requirement</th>
                <th>Status</th>
            </tr>
        </thead>

        <tbody>
            <tr><td>TypeScript frontend</td><td class="status">✅</td></tr>
            <tr><td>Vite configuration</td><td class="status">✅</td></tr>
            <tr><td>Strict TypeScript</td><td class="status">✅</td></tr>
            <tr><td>Typed product interface</td><td class="status">✅</td></tr>
            <tr><td>DOM manipulation</td><td class="status">✅</td></tr>
            <tr><td>Null checks for DOM elements</td><td class="status">✅</td></tr>
            <tr><td>Typed HTML elements</td><td class="status">✅</td></tr>
            <tr><td>Form handling</td><td class="status">✅</td></tr>
            <tr><td><code>preventDefault()</code></td><td class="status">✅</td></tr>
            <tr><td>Asynchronous requests</td><td class="status">✅</td></tr>
            <tr><td><code>async/await</code></td><td class="status">✅</td></tr>
            <tr><td><code>try/catch</code> error handling</td><td class="status">✅</td></tr>
            <tr><td><code>response.ok</code> validation</td><td class="status">✅</td></tr>
            <tr><td>Loading feedback</td><td class="status">✅</td></tr>
            <tr><td>Error feedback</td><td class="status">✅</td></tr>
            <tr><td>Empty state</td><td class="status">✅</td></tr>
            <tr><td>Dynamic product rendering</td><td class="status">✅</td></tr>
            <tr><td>Product search</td><td class="status">✅</td></tr>
            <tr><td>Modular frontend structure</td><td class="status">✅</td></tr>
            <tr><td>Java HTTP API</td><td class="status">✅</td></tr>
            <tr><td>Frontend/API integration</td><td class="status">✅</td></tr>
            <tr><td>Production build</td><td class="status">✅</td></tr>
        </tbody>
    </table>

    <div class="note">
        The frontend was intentionally kept visually simple at this stage.
        The main objective of this milestone is to establish a functional,
        typed and modular web interface connected to the existing scraper.
    </div>
</section>


<!-- CURRENT OUTPUT -->

<section>
    <h2>💻 Current Output</h2>

    <p>
        The original scraper prints each product in a readable format.
    </p>

    <p>
        The number of products and their information depend on the HTML
        returned by Falabella at execution time.
    </p>

    <p>
        The web frontend displays the same product information through
        the browser and allows the user to filter products by name.
    </p>
</section>


<!-- VISION -->

<section>
    <h2>🌟 Project Vision</h2>

    <p>
        The long-term goal of WebScraper is to evolve from a single-store
        scraper into a complete product and price intelligence platform.
    </p>

    <p>
        The platform is intended to collect, store, compare and analyze
        products from multiple online retailers.
    </p>

    <p>Users will eventually be able to:</p>

    <ul>
        <li>Explore products from different categories</li>
        <li>Search and filter available products</li>
        <li>Compare prices across multiple stores</li>
        <li>Review historical price changes</li>
        <li>Identify genuine discounts</li>
        <li>Discover the best current offers</li>
        <li>Receive intelligent product recommendations</li>
    </ul>
</section>


<!-- FUTURE -->

<section>
    <h2>🗺️ Future Improvements</h2>

    <h3>🔍 General Scraping</h3>

    <ul class="future">
        <li>Support any Falabella product category.</li>
        <li>Allow users to provide any compatible category URL.</li>
        <li>Automatically discover and process products from category pages.</li>
        <li>Add pagination support.</li>
        <li>Extract product page URLs.</li>
        <li>Extract product image URLs.</li>
        <li>Extract additional product specifications.</li>
        <li>Improve parser resilience against HTML structure changes.</li>
        <li>Prevent duplicated products in the final result.</li>
    </ul>

    <h3>💾 Data Persistence</h3>

    <ul class="future">
        <li>Integrate a relational database.</li>
        <li>Persist products and store information.</li>
        <li>Maintain a complete price history for every product.</li>
        <li>Register the date and time of each detected price.</li>
        <li>Detect price increases and reductions.</li>
        <li>Identify the lowest historical price.</li>
        <li>Avoid duplicate records during repeated scraping operations.</li>
    </ul>

    <h3>🌱 Spring Boot Web Platform</h3>

    <ul class="future">
        <li>Migrate the application entry point to Spring Boot.</li>
        <li>Replace the current lightweight HTTP adapter with a production-oriented REST API.</li>
        <li>Expand the existing web interface.</li>
        <li>Add product search and category filtering.</li>
        <li>Add store filters.</li>
        <li>Add price-based sorting.</li>
        <li>Display product details.</li>
        <li>Show price-history charts.</li>
        <li>Create dashboards with product and market statistics.</li>
    </ul>

    <h3>🏪 Multi-Store Comparison</h3>

    <ul class="future">
        <li>Add scrapers for additional e-commerce websites.</li>
        <li>Normalize products collected from different stores.</li>
        <li>Identify equivalent products across retailers.</li>
        <li>Compare prices between stores.</li>
        <li>Automatically highlight the best available offer.</li>
        <li>Generate product rankings based on prices and discounts.</li>
        <li>Track availability and stock when possible.</li>
    </ul>

    <h3>🤖 Artificial Intelligence</h3>

    <ul class="future">
        <li>Integrate AI-powered product recommendations.</li>
        <li>Suggest alternatives according to price and characteristics.</li>
        <li>Analyze historical price behavior.</li>
        <li>Detect potentially misleading discounts.</li>
        <li>Identify relevant buying opportunities.</li>
        <li>Generate summaries of product characteristics.</li>
        <li>Recommend products based on user preferences.</li>
        <li>Provide intelligent insights for purchasing decisions.</li>
    </ul>
</section>


<!-- AUTHOR -->

<section>
    <h2>👨‍💻 Author</h2>

    <p>
        <strong>Diego Reyes</strong>
    </p>

    <p>
        Developed as a Java web scraping project focused on software
        architecture, automated testing, TypeScript frontend development
        and future scalability.
    </p>
</section>


<footer>
    WebScraper · Diego Reyes
</footer>
