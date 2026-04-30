/**
 * Função para rolar suavemente até o topo da página
 * @param {Event} event - O evento do clique
 */
function scrollToTop(event) {
    event.preventDefault();  // Impede o comportamento padrão do link
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

/**
 * Mostra apenas as músicas do álbum selecionado no formulário de playlist.
 */
document.addEventListener('DOMContentLoaded', function () {
    // Processar formulário de criação de playlist
    const criarPlaylistContainer = document.getElementById('form-criar-playlist');
    if (criarPlaylistContainer) {
        const select = criarPlaylistContainer.querySelector('.album-select');
        const articles = criarPlaylistContainer.querySelectorAll('.album-article');
        
        if (select && articles.length > 0) {
            select.addEventListener('change', function () {
                articles.forEach(article => {
                    article.hidden = article.dataset.album !== this.value;
                });
            });
        }
    }
    
    // Processar formulário de atualização de playlist
    const atualizarPlaylistContainer = document.getElementById('form-atualizar-playlist');
    if (atualizarPlaylistContainer) {
        const select = atualizarPlaylistContainer.querySelector('.album-select');
        const articles = atualizarPlaylistContainer.querySelectorAll('.album-article');
        
        if (select && articles.length > 0) {
            select.addEventListener('change', function () {
                articles.forEach(article => {
                    article.hidden = article.dataset.album !== this.value;
                });
            });
        }
    }
});

