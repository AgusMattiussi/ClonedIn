import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import ExperienceForm from "../../views/userExperienceForm";


describe("Test Experience Form", () => {

    test("Test page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <ExperienceForm />
            </BrowserRouter>
        );
        expect(screen.getByText("Experience")).toBeInTheDocument()
    })

    test("Test sumbit with empty fields", async () => {
        render(
            <BrowserRouter>
                <ExperienceForm />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("experience-form-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <ExperienceForm />
            </BrowserRouter>
        );

        const experience = {
            enterpriseName: "JP",
            position: "Apprentice",
            description: "Nice",
        }

        const company = screen.getByPlaceholderText("Enterprise Name*")
        fireEvent.change(company, {target: {value: experience.enterpriseName}})

        const position = screen.getByPlaceholderText("Position*")
        fireEvent.change(position, {target: {value: experience.position}})

        const description = screen.getByPlaceholderText("Description")
        fireEvent.change(description, {target: {value: experience.description}})

        expect((screen.getByPlaceholderText("Enterprise Name*") as HTMLInputElement).value).toBe(experience.enterpriseName);
        expect((screen.getByPlaceholderText("Position*") as HTMLInputElement).value).toBe(experience.position);
        expect((screen.getByPlaceholderText("Description") as HTMLInputElement).value).toBe(experience.description);

    });
});