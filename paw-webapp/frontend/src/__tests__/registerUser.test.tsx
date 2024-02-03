import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import RegisterUser from "../views/userRegister";


describe("Test Register User", () => {

    test("Test register user page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <RegisterUser />
            </BrowserRouter>
        );
        expect(screen.getByText("Basic Information")).toBeInTheDocument()
    })

    test("Test register user fails with empty credentials", async () => {
        render(
            <BrowserRouter>
                <RegisterUser />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("register-user-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })
});