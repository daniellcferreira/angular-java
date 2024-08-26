class TituloDinamico extends HTMLElement {
  constructor() {
    super();

    const shadow = this.attachShadow({ mode: "open" });

    // base do componente
    const componentRoot = document.createElement("h1");
    componentRoot.textContent = "teste";

    // estilizando o componente
    const style = document.createElement("style");
    style.textContent = `
     h1{
        color: red;
     }
    `;

    // enviando para o shadow
    shadow.appendChild(componentRoot);
    shadow.appendChild(style);
  }
}

customElements.define("titulo-dinamico", TituloDinamico);
