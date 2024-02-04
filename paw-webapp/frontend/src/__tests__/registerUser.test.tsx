import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import RegisterUser from "../views/userRegister";
import { getUserMockedData } from "../__mocks__/mockedData";


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

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <RegisterUser />
            </BrowserRouter>
        );
       
        const user = getUserMockedData()

        const email = screen.getByPlaceholderText("Email*")
        fireEvent.change(email, {target: {value: user.email}})

        const name = screen.getByPlaceholderText("Name*")
        fireEvent.change(name, {target: {value: user.name}})
        
        const password = screen.getByPlaceholderText("Password*")
        fireEvent.change(password, {target: {value: user.password}})  

        const repeatPassword = screen.getByPlaceholderText("Repeat Password*")
        fireEvent.change(repeatPassword, {target: {value:  user.password}}) 

        expect((screen.getByPlaceholderText("Email*") as HTMLInputElement).value).toBe(user.email);
        expect((screen.getByPlaceholderText("Name*") as HTMLInputElement).value).toBe(user.name);
        expect((screen.getByPlaceholderText("Password*") as HTMLInputElement).value).toBe(user.password);
        expect((screen.getByPlaceholderText("Repeat Password*") as HTMLInputElement).value).toBe(user.password);

    });
});