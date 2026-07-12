export function getErrorMessage(error, fallback = "Something went wrong") {
    return (
        error?.friendlyMessage ||
        error?.response?.data?.message ||
        error?.message ||
        fallback
    );
}
