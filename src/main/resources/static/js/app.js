
// Check if passwords match
$(document).ready(function() {
    $('.retype-password').keyup(function() {
        let password = $('.password').val();
        let retypePassword = $('.retype-password').val();

        if (password === retypePassword) {
            $('.valid-password').text('Passwords match').css('color', 'green');
            $('#submit').prop('disabled', false);
        } else {
            $('.valid-password').text('Passwords do not match').css('color', 'red');
            $('#submit').prop('disabled', true);
        }
    });
});

// Disable Publish Post btn if title or content or tags are empty
$(document).ready(function() {
    // Add event listeners to input fields and checkboxes
    $('input[type="text"], textarea, input[type="checkbox"]').on('keyup change', function() {
        updateSubmitButtonState();
    });

    // Add event listener to TinyMCE editor
    tinymce.get('content').on('keyup change', function() {
        updateSubmitButtonState();
    });

    function updateSubmitButtonState() {
        let title = $('#title').val();
        let content = tinymce.get('content').getContent();
        let tags = $('input[type="checkbox"]:checked').length;

        // Check if title, content, and at least one tag are not empty
        if (title.trim() !== '' && content.trim() !== '' && tags > 0) {
            // Enable the Publish Post button
            $('#submit').prop('disabled', false);
        } else {
            // Disable the Publish Post button
            $('#submit').prop('disabled', true);
        }
    }
});