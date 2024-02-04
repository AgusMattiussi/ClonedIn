import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import RegisterEnterprise from "../views/enterpriseRegister";
import { getEnterpriseMockedData } from "../__mocks__/mockedData";


describe("Test Register User", () => {

    test("Test register enterprise page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <RegisterEnterprise />
            </BrowserRouter>
        );
        expect(screen.getByText("Basic Information")).toBeInTheDocument()
    })

    test("Test register enterprise fails with empty credentials", async () => {
        render(
            <BrowserRouter>
                <RegisterEnterprise />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("register-enterprise-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <RegisterEnterprise />
            </BrowserRouter>
        );
       
        const enterprise = getEnterpriseMockedData()

        const email = screen.getByPlaceholderText("Email*")
        fireEvent.change(email, {target: {value: enterprise.email}})

        const name = screen.getByPlaceholderText("Enterprise Name*")
        fireEvent.change(name, {target: {value: enterprise.name}})
        
        const password = screen.getByPlaceholderText("Password*")
        fireEvent.change(password, {target: {value: enterprise.password}})  

        const repeatPassword = screen.getByPlaceholderText("Repeat Password*")
        fireEvent.change(repeatPassword, {target: {value: enterprise.password}}) 

        expect((screen.getByPlaceholderText("Email*") as HTMLInputElement).value).toBe(enterprise.email);
        expect((screen.getByPlaceholderText("Enterprise Name*") as HTMLInputElement).value).toBe(enterprise.name);
        expect((screen.getByPlaceholderText("Password*") as HTMLInputElement).value).toBe(enterprise.password);
        expect((screen.getByPlaceholderText("Repeat Password*") as HTMLInputElement).value).toBe(enterprise.password);

    });
});