describe('Register spec', () => {

  it('Register successful', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {}
    });

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("john.doe@mail.com");
    cy.get('input[formControlName=password]').type("test1234{enter}");

    cy.url().should('include', '/login');
  });

  it('Register error shows message', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Validation error' }
    });

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("john.doe@mail.com");
    cy.get('input[formControlName=password]').type("invalid{enter}");

    cy.get('.error').should('contain', 'An error occurred');
  });
});
