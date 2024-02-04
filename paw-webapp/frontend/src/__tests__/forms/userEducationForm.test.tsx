import { act, fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import EducationForm from "../../views/userEducationForm";


describe("Test Education Form", () => {

    test("Test page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <EducationForm />
            </BrowserRouter>
        );
        expect(screen.getByText("Educacion")).toBeInTheDocument()
    })

    test("Test sumbit with empty fields", async () => {
        render(
            <BrowserRouter>
                <EducationForm />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("education-form-button")
        await act(async () => {
            fireEvent.click(btn)
        })
        expect(screen.findByText("/required/i")).not.toBeNull();
    })

    test("Test that required fields are set correctly", async () => {
        render(
            <BrowserRouter>
                <EducationForm />
            </BrowserRouter>
        );

        const education = {
            college: "ITBA",
            degree: "IT",
            comment: "Nice",
        }

        const college = screen.getByPlaceholderText("Institution*")
        fireEvent.change(college, {target: {value: education.college}})

        const degree = screen.getByPlaceholderText("Degree*")
        fireEvent.change(degree, {target: {value: education.degree}})

        const comment = screen.getByPlaceholderText("Comment")
        fireEvent.change(comment, {target: {value: education.comment}})

        expect((screen.getByPlaceholderText("Institution*") as HTMLInputElement).value).toBe(education.college);
        expect((screen.getByPlaceholderText("Degree*") as HTMLInputElement).value).toBe(education.degree);
        expect((screen.getByPlaceholderText("Comment") as HTMLInputElement).value).toBe(education.comment);

    });
});