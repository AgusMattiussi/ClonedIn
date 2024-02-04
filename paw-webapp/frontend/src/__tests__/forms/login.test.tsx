import { fireEvent, render, screen } from "@testing-library/react";
import Login from "../../views/login";;
import { BrowserRouter } from "react-router-dom";

describe("Test Login", () => {

    test("Test login page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
        expect(screen.getByText("Welcome to ClonedIN!")).toBeInTheDocument()
    })

    test("Test login fails with empty credentials", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("login-button")
        fireEvent.click(btn)
        const errorMessage = await screen.findByText("Invalid Credentials")
        expect(errorMessage).toBeInTheDocument()
    })

    test("Test email and password are set correctly", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
        const user = {
            email: "paw-2022b-4@gmail.com",
            password: "paw-2022b-4",
        };
        const email = screen.getByPlaceholderText("Email")
        fireEvent.change(email, {
            target: {
                value: user.email
            }
        })
        const password = screen.getByPlaceholderText("Password")
        fireEvent.change(password, {
            target: {
                value: user.password
            }
        })        
        expect((screen.getByPlaceholderText("Email") as HTMLInputElement).value).toBe(user.email);
        expect((screen.getByPlaceholderText("Password") as HTMLInputElement).value).toBe(user.password);
    });
});