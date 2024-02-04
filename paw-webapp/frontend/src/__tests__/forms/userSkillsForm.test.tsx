import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import SkillsForm from "../../views/userSkillsForm";


describe("Test Skills Form", () => {

    test("Test page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <SkillsForm />
            </BrowserRouter>
        );
        expect(screen.getByText("Skill")).toBeInTheDocument()
    })

    test("Test sumbit with empty fields", async () => {
        render(
            <BrowserRouter>
                <SkillsForm />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("skills-form-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <SkillsForm />
            </BrowserRouter>
        );

        const skill = screen.getByPlaceholderText("Skill Ex")
        fireEvent.change(skill, {target: {value: "French"}})

        expect((screen.getByPlaceholderText("Skill Ex") as HTMLInputElement).value).toBe("French");

    });
});