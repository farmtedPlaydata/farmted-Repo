import React from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

interface EditorComponentProps {
  value: string;
  onChange: (value: string) => void;
}

const EditorComponent: React.FC<EditorComponentProps> = ({ value, onChange }) => {
  const modules = {
    toolbar: [
      [{ 'header': [1, 2, false] }],
      ['bold', 'italic', 'underline', 'strike', 'blockquote'],
      [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}],
      ['link'],
      [{ 'align': [] }, { 'color': [] }, { 'background': [] }],
      ['clean']
    ],
  };

  const formats = [
    'header',
    'bold', 'italic', 'underline', 'strike', 'blockquote',
    'list', 'bullet', 'indent',
    'link',
    'align', 'color', 'background',
  ];

  return (
    <div style={{ height: "400px" }}>
      <ReactQuill
        style={{ height: "350px" }}
        theme="snow"
        modules={modules}
        formats={formats}
        value={value || ''}
        onChange={(content: string, delta: any, source: string, editor: any) => onChange(editor.getHTML())}
      />
    </div>
  );
};

export default EditorComponent;
