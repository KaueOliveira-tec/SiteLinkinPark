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
    const albumSelects = document.querySelectorAll('.album-select');
    if (!albumSelects.length) {
        return;
    }

    albumSelects.forEach(select => {
        const albumArticles = select.closest('form')?.querySelectorAll('.album-article') || document.querySelectorAll('.album-article');
        select.addEventListener('change', function () {
            const selectedAlbum = this.value;
            albumArticles.forEach(article => {
                article.hidden = article.dataset.album !== selectedAlbum;
            });
        });
    });
});

