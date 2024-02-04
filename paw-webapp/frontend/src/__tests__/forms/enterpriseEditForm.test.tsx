import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import JobOfferForm from "../../views/enterpriseJobOfferForm";


describe("Test Skills Form", () => {

    test("Test page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <JobOfferForm/>
            </BrowserRouter>
        );
        expect(screen.getByText("JobOffer add")).toBeInTheDocument()
    })

    test("Test sumbit with empty fields", async () => {
        render(
            <BrowserRouter>
                <JobOfferForm/>
            </BrowserRouter>
        );
        const btn = screen.getByTestId("job-offer-form-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <JobOfferForm/>
            </BrowserRouter>
        );

        const position = screen.getByPlaceholderText("Position*")
        fireEvent.change(position, {target: {value: "Backend Developer"}})

        expect((screen.getByPlaceholderText("Position*") as HTMLInputElement).value).toBe("Backend Developer");

    });
});