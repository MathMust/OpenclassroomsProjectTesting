describe('Not Found Page', () => {

  it('should display the 404 message when route does not exist', () => {

    // Va sur une URL inconnue
    cy.visit('/this-route-does-not-exist', { failOnStatusCode: false });

    // Vérifie l'affichage du message
    cy.contains('Page not found !').should('exist');

    // Vérifie que l'élément est centré via la classe flex (optionnel)
    cy.get('div.flex.justify-center.mt3').should('exist');
  });

});
