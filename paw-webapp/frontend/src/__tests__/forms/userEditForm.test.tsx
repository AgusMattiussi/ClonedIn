import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import EditUserForm from "../../views/userEditForm";

describe("Test User Edit Form", () => {

    test("Test page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <EditUserForm/>
            </BrowserRouter>
        );
        expect(screen.getByText("Edit")).toBeInTheDocument()
    })
});