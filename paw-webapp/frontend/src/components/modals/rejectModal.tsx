export default function RejectModal(props: any) {
  return (
    <div className="modal fade" id="rejectModal" aria-labelledby="rejectApplicationModalLabel" aria-hidden="true">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title" id="rejectApplicationModalLabel">
              {props.title}
            </h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div className="modal-body">{props.msg}</div>
          <div className="modal-footer justify-content-center">
            <button
              type="button"
              className="btn btn-secondary"
              data-bs-dismiss="modal"
              style={{ whiteSpace: "normal", marginBottom: "0.75rem", width: "200px" }}
            >
              {props.cancel}
            </button>
            <button
              className="btn btn-danger"
              style={{ whiteSpace: "normal", marginBottom: "0.75rem", width: "200px" }}
              onClick={props.onConfirmClick}
            >
              {props.confirm}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
