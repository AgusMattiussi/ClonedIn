import { render, screen } from "@testing-library/react";
import Login from "../views/login";
import { BrowserRouter } from "react-router-dom";

describe("Test login", () => {
    test("Test login page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );

        expect(screen.getByText("Welcome to ClonedIN!")).toBeInTheDocument()
    })
});