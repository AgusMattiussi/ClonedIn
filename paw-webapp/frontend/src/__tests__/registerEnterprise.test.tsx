import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import RegisterEnterprise from "../views/enterpriseRegister";


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
});