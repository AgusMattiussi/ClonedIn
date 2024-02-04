import { fireEvent, render, screen } from "@testing-library/react";
import RejectModal from "../modals/rejectModal";

let propsMap: { title: any; msg: any; cancel: any; confirm: any; onConfirmClick?: jest.Mock<any, any>; }

const customPropsMap = (options = {}) => {
    const map = {
        title: "Reject Modal",
        msg: "Reject this modal?",
        cancel: "Cancel",
        confirm: "Confirm",
        onConfirmClick: jest.fn()
    }

    return { ...map, ...options };
};


describe("Test Reject Modal", () => {
    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Test that modal renders correctly', ()=>{
        render(<RejectModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
    })

    test('Test that all props are visible', ()=>{
        render(<RejectModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
        expect(screen.getByText(propsMap.msg)).toBeInTheDocument()
        expect(screen.getByText(propsMap.cancel)).toBeInTheDocument()
        expect(screen.getByText(propsMap.confirm)).toBeInTheDocument()
    })

    test('Test that onConfirmClick is run after clicking confirm button', ()=>{
        render(<RejectModal {...propsMap}/>)
        const button = screen.getByTestId("confirm-button")
        fireEvent.click(button)
        expect(propsMap.onConfirmClick).toHaveBeenCalled()
    })
})