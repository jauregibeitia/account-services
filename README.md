# Account-services

## Description

Account Services is a simple REST API that allows you to perform the following operations:
1. Create an account.
2. Update an existing account.
3. Retrieve account details by ID.
4. List all stored accounts.
5. Transfer funds between accounts.

## Create a new account

### Request

`POST /accounts/create`

Input field | Description                                                                                            | 
--- |--------------------------------------------------------------------------------------------------------| 
Account Name | A non-null and non-blank string.                                                                       | 
Currency code | A valid ISO 4217 currency code.                                                                                 | 
Treasury | Either `true` or `false`. If set to `true`, the new account will be marked as a treasury account | 

#### Example Request Body

```json
{
  "accountName": "Savings Account",
  "currencyCode": "USD",
  "treasury": true
}
```
### Response

    HTTP/1.1 201 Created

The endpoint returns the newly created account with a randomly generated UUID as `accountId` and its balance set to zero.

## Retrieve an Account by ID

### Request

`GET /accounts/{accountId}`

Input field | Description                                   | 
--- |-----------------------------------------------| 
Account Id | Must be an id already stored in the database. | 


### Response

    HTTP/1.1 200 OK

The endpoint returns the account details.

If no account with such `accountId` is found, an `AccountNotFoundException` will be thrown.

## List All Stored Accounts

### Request

`GET /accounts/`

### Response

    HTTP/1.1 200 OK

The endpoint returns all stored accounts.

If no account is found, an empty list will be returned.

## Move Funds Between Accounts

### Request

`POST /accounts/move-funds`

Input field | Description                                                                                                                     | 
--- |---------------------------------------------------------------------------------------------------------------------------------| 
Source Account Id | Must be a valid UUID corresponding to an existing account in the database.| 
Target Account Id | Must be a valid UUID corresponding to an existing account in the database.| 
Amount | The amount to be transferred from the source account to the target account. This amount is specified in the source account's currency. | 

#### Example Request Body

```json
{
  "sourceAccountId": "123e4567-e89b-12d3-a456-426614174000",
  "targetAccountId": "123e4567-e89b-12d3-a456-426614174001",
  "amount": 100.0
}
```
### Response

    HTTP/1.1 202 ACCEPTED

The endpoint returns the updated source account with the new balance.

If either the source or target account IDs are not found, an `AccountNotFoundException` will be thrown.

### Business Rules

- Only treasury accounts (Treasury property) can have a negative balance. If a non-treasury account attempts to transfer an amount greater than its balance, a `NegativeBalanceForNonTreasuryAccountException` will be thrown.
- `ExchangeRateHttpClient`: Retrieves the exchange rate to convert the amount from the source account's currency to the target account's currency.

## Update an Account

### Request

`POST /accounts/update`

Input field | Description                                             | 
--- |---------------------------------------------------------| 
Account Id | Specifies which account will be updated.| 
Account Name | New account name.                                       | 
Amount | New currency code.                                      | 

#### Example Request Body

```json
{
  "accountId": "123e4567-e89b-12d3-a456-426614174000",
  "accountName": "New Account Name",
  "currencyCode": "EUR"
}
```

### Response

    HTTP/1.1 200 OK

The endpoint returns the updated account with the new values.

If no account with such `accountId` is found, an `AccountNotFoundException` will be thrown.

### Business Rules

- The treasury property and account balance cannot be updated using this endpoint.
- `ExchangeRateHttpClient`:If a new currency code is specified, ExchangeRateHttpClient will retrieve the rate to convert the account balance to the new currency.


